package cn.iocoder.oceans.order.service.impl;

import cn.iocoder.oceans.core.base.mq.TransactionMQProducer;
import cn.iocoder.oceans.coupon.api.CouponService;
import cn.iocoder.oceans.order.api.OrderService;
import cn.iocoder.oceans.order.api.dto.OrderCreateDTO;
import cn.iocoder.oceans.order.service.dao.OrderMapper;
import cn.iocoder.oceans.order.service.po.OrderItemPO;
import cn.iocoder.oceans.order.service.po.OrderPO;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单 Service 实现类
 *
 * 参考文章
 *
 * 1. https://blog.csdn.net/dreamvyps/article/details/50325967
 * 2. https://blog.csdn.net/chunlongyu/article/details/53844393
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private TransactionMQProducer mqProducer;
    @Autowired
    private OrderMapper orderMapper;
    @Reference
    private CouponService couponService;

    @Override
    public void createOrder(OrderCreateDTO orderCreateDTO) throws Exception {
        // 校验库存是否足够
        // 校验优惠劵是否可用
        couponService.valid(orderCreateDTO.getCouponId());
        // 创建 OrderApply
        OrderPO order = createInitOrder(orderCreateDTO);
        // 创建 OrderPO 订单
        createOrder(order);
        // TODO 转换返回
    }

    /**
     * 新增一条初始化的订单
     *
     * @return 初始化的定的那
     */
    // @Transactional // 事务 A 。如果后面要插入多张表，要加下事务注解
    protected OrderPO createInitOrder(OrderCreateDTO orderCreateDTO) {
        OrderPO orderPO = new OrderPO().setStatus(OrderPO.STATUS_INIT)
                .setCouponId(orderCreateDTO.getCouponId());
        orderMapper.insert(orderPO);
        return orderPO;
    }

    public OrderPO createOrder(OrderPO order) throws Exception {
        SendResult result = null;
        try {
            // 发送订单创建成功的预消息
            result = mqProducer.sendTransactionMessage(null); // TODO 具体消息
            // 创建订单
            initOrder(order);
            // 提交订单创建成功的消息
            mqProducer.commitTransactionMessage(result);
            return order;
        } catch (Exception e) {
            // 回滚订单创建成功的消息
            if (result != null) {
                mqProducer.rollbackTransactionMessage(result, e);
            }
            // async 发送，订单创建失败的消息。因为会有定时任务，轮询 OrderPO 表，所以此处即使发送失败，也没问题。
            // 发这条的目的是，更快的释放订单相关的资源。例如，库存，优惠劵
            mqProducer.send(null, new SendCallback() { // TODO 具体消息

                @Override
                public void onSuccess(SendResult sendResult) {
                    if (sendResult.getSendStatus() == SendStatus.SEND_OK) {
                        // 更新订单删除
                        delete(order.getOrderId());
                    }
                }

                @Override
                public void onException(Throwable e) {
                    e.printStackTrace(); // TODO 打印下日志
                }
            });
            // 抛出异常
            throw e;
        }
    }

    @Transactional // 事务 B 。需要保证 `#createInitOrder()` 和 `#createOrder(OrderPO order)` 方法，是在两个事物。不然就被回滚了
    public OrderPO initOrder(OrderPO order) {
        // 锁定库存。锁定失败，抛出异常 TODO
        // 使用优惠劵。使用失败，抛出异常
        couponService.valid(order.getCouponId());
        // 更新订单 TODO 更新
        OrderPO updateOrder = new OrderPO().setStatus(OrderPO.STATUS_WAITING_FOR_PAYMENT);
        orderMapper.update(updateOrder);
        // 插入订单明细
        List<OrderItemPO> orderItems = new ArrayList<>();
        // TODO 入库
        return null;
    }

    @Scheduled(fixedRate = 1000) // TODO 未来，需要改成
    public void pollingTimeoutOrderApply() {
        // 从 db 轮询，5 分钟时间，并且处于初始化状态的订单 SELECT xx FROM orders WHERE status = 1 AND create_time 超过 5 分钟
        List<OrderPO> orders = new ArrayList<>(); // TODO from db
        orders.forEach(order -> {
            // 同步发送消息
            try {
                mqProducer.send(new Message());
                // 然后，不同消费者集群，订阅不同的消息。
                // 例如，优惠劵，返还优惠劵
                // 例如，库存消费者，返还库存
            } catch (Exception e) {
                throw new RuntimeException(e); // TODO 处理异常
            }
            // 更新订单删除
            delete(order.getOrderId());
        });
    }

    private void delete(Long orderId) {
        OrderPO updateOrder = new OrderPO().setOrderId(orderId).setStatus(OrderPO.STATUS_DELETE);
        orderMapper.update(updateOrder);
    }

}
