package cn.iocoder.oceans.core.base.mq;

import org.apache.rocketmq.client.Validators;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.impl.producer.DefaultMQProducerImpl;
import org.apache.rocketmq.client.log.ClientLogger;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.MessageAccessor;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.logging.InternalLogger;
import org.apache.rocketmq.remoting.RPCHook;
import org.springframework.util.ReflectionUtils;

public class TransactionMQProducer extends DefaultMQProducer {

    private final InternalLogger log = ClientLogger.getLog();

    private TransactionListener transactionListener;
    private DefaultMQProducer defaultMQProducer;

    public TransactionMQProducer() {
    }

    public TransactionMQProducer(final String producerGroup) {
        super(producerGroup);
    }

    public TransactionMQProducer(final String producerGroup, RPCHook rpcHook) {
        super(producerGroup, rpcHook);
    }

    @Override
    public void start() throws MQClientException {
        this.defaultMQProducerImpl.initTransactionEnv();
        super.start();
        // 设置 defaultMQProducer
        this.defaultMQProducer = (DefaultMQProducer) ReflectionUtils.getField(
                ReflectionUtils.findField(DefaultMQProducerImpl.class, "defaultMQProducer"),
                this.defaultMQProducerImpl
        );
    }

    @Override
    public void shutdown() {
        super.shutdown();
        this.defaultMQProducerImpl.destroyTransactionEnv();
    }


//    @Override
//    public TransactionSendResult sendMessageInTransaction(final Message msg, final Object arg) throws MQClientException {
//        if (null == this.transactionListener) {
//            throw new MQClientException("TransactionListener is null", null);
//        }
//
//        return this.defaultMQProducerImpl.sendMessageInTransaction(msg, transactionListener, arg);
//    }

    public TransactionListener getTransactionListener() {
        return transactionListener;
    }

    public void setTransactionListener(TransactionListener transactionListener) {
        this.transactionListener = transactionListener;
    }

    public TransactionSendResult sendTransactionMessage(MessageExt msg) throws MQClientException {
        Validators.checkMessage(msg, defaultMQProducer);

        SendResult sendResult = null;
        MessageAccessor.putProperty(msg, MessageConst.PROPERTY_TRANSACTION_PREPARED, "true");
        MessageAccessor.putProperty(msg, MessageConst.PROPERTY_PRODUCER_GROUP, this.getProducerGroup());
        try {
            sendResult = this.send(msg);
        } catch (Exception e) {
            throw new MQClientException("send message Exception", e);
        }

        LocalTransactionState localTransactionState = LocalTransactionState.UNKNOW;
//        Throwable localException = null;
        switch (sendResult.getSendStatus()) {
            case SEND_OK: {
                if (sendResult.getTransactionId() != null) {
                    msg.putUserProperty("__transactionId__", sendResult.getTransactionId());
                }
                String transactionId = msg.getProperty(MessageConst.PROPERTY_UNIQ_CLIENT_MESSAGE_ID_KEYIDX);
                if (null != transactionId && !"".equals(transactionId)) {
                    msg.setTransactionId(transactionId);
                }
            }
            break;
//            case FLUSH_DISK_TIMEOUT:
//            case FLUSH_SLAVE_TIMEOUT:
//            case SLAVE_NOT_AVAILABLE:
//                localTransactionState = LocalTransactionState.ROLLBACK_MESSAGE;
//                break;
            default:
//                break;
                throw new MQClientException("send message success, but send result is " + sendResult.getSendStatus(), null);
        }

        TransactionSendResult transactionSendResult = new TransactionSendResult();
        transactionSendResult.setSendStatus(sendResult.getSendStatus());
        transactionSendResult.setMessageQueue(sendResult.getMessageQueue());
        transactionSendResult.setMsgId(sendResult.getMsgId());
        transactionSendResult.setQueueOffset(sendResult.getQueueOffset());
        transactionSendResult.setTransactionId(sendResult.getTransactionId());
        transactionSendResult.setLocalTransactionState(localTransactionState);
        return transactionSendResult;
    }

    public void commitTransactionMessage(SendResult sendResult) {
        try {
            this.defaultMQProducerImpl.endTransaction(sendResult, LocalTransactionState.COMMIT_MESSAGE, null);
        } catch (Exception e) {
            log.warn("local transaction execute " + LocalTransactionState.ROLLBACK_MESSAGE + ", but end broker transaction failed", e);
        }
    }

    public void rollbackTransactionMessage(SendResult sendResult, Throwable localException) {
        try {
            this.defaultMQProducerImpl.endTransaction(sendResult, LocalTransactionState.ROLLBACK_MESSAGE, localException);
        } catch (Exception e) {
            log.warn("local transaction execute " + LocalTransactionState.ROLLBACK_MESSAGE + ", but end broker transaction failed", e);
        }
    }

}
