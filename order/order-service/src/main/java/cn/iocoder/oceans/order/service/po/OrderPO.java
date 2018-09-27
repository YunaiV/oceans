package cn.iocoder.oceans.order.service.po;

import cn.iocoder.oceans.core.po.BasePO;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 订单主体 PO
 */
@Data
@Accessors(chain = true)
public class OrderPO extends BasePO {

    /**
     * 状态 - 初始化
     */
    public static final Integer STATUS_INIT = 1;
    /**
     * 状态 - 等待支付
     */
    public static final Integer STATUS_WAITING_FOR_PAYMENT = 2;
    /**
     * 状态 - 已支付，等待发货
     */
    public static final Integer STATUS_PAID = 3;
    /**
     * 状态 - 已发货，等待确认收货
     */
    public static final Integer STATUS_WAITING_FOR_SIGN = 4;
    /**
     * 状态 - 已确认收货，即完成
     */
    public static final Integer STATUS_SIGNED = 100;
    /**
     * 状态 - 订单关闭。例如，用户取消订单
     */
    public static final Integer STATUS_CLOSED = 99;
    /**
     * 状态 - 删除。例如，{@link #STATUS_INIT} 初始化的订单
     */
    public static final Integer STATUS_DELETE = 200;

    /**
     * 订单编号
     */
    private Long orderId;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 关闭类型
     */
    private Integer closeType;
    /**
     * 优惠劵编号
     */
    private Long couponId;

}