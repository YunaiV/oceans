package cn.iocoder.oceans.order.service.config;

import cn.iocoder.oceans.core.base.mq.TransactionMQProducer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfiguration {

    @Bean
    public TransactionMQProducer mqProducer() throws MQClientException {
        String group = "TODO";
        String namesrvAddr = "TODO";
        TransactionMQProducer producer = new TransactionMQProducer(group);
        producer.setNamesrvAddr(namesrvAddr);
        // TODO 芋艿
        producer.setTransactionListener(new TransactionListener() {

            @Override
            public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
                // TODO 芋艿，此处这么写的原因是，rocketmq 这样的模板方法，无法获得返回值。
                // TODO 另外，不同消息，应该有不同的 TransactionListener ，需要进行下封装
                return LocalTransactionState.UNKNOW;
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt msg) {
                // TODO 芋艿，此处有两种方式实现
                // 1. 每个消息对应的业务 Service ，自己判断是否完成事务。例如，创建订单成功的 Message ，调用 OrderService 判断这个订单是否存在
                // 2. 统一，每个库有个 sys_mq_message 表，每次发送事务消息，都和业务逻辑的事务在一起，插入一条消息。从而和事务一起提交。那么事务提交了，也就意味着事务消息可以回查到。否则，查询不到。
                return null;
            }

        });
        producer.start();
        return producer;
    }

}