package org.example.learn.rabbitmq.rpc.hello;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.utility.BlockingCell;;


public class RPCClient {

    private final Map<String, BlockingCell<Object>> _contibuationMap = new HashMap<String, BlockingCell<Object>>();
    private int correlationId = 0;
    private Connection connection;
    private Channel channel;
    private String requestQueuename = "rpc_queue";
    private String replyQueueName;
    private DefaultConsumer _consumer;

    public RPCClient() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RabbitConfig.ip);
        factory.setPort(RabbitConfig.port);
        factory.setUsername(RabbitConfig.username);
        factory.setPassword(RabbitConfig.password);

        connection = factory.newConnection();
        channel = connection.createChannel();

        replyQueueName = channel.queueDeclare().getQueue();
        _consumer = setupConsumer();
    }

    public String call(String message) throws IOException, ShutdownSignalException, ConsumerCancelledException,
            InterruptedException, TimeoutException {
        if (_consumer == null)
            return null;
        BlockingCell<Object> k = new BlockingCell<Object>();
        BasicProperties props;
        synchronized (_contibuationMap) {
            correlationId++;
            String corrId = "" + correlationId;
            props = new BasicProperties.Builder()
                    .correlationId(corrId)
                    .replyTo(replyQueueName)
                    .build();
            _contibuationMap.put(corrId, k);
        }

        channel.basicPublish("", requestQueuename, props, message.getBytes());

        Object reply = k.uninterruptibleGet(4000);
        if (reply instanceof ShutdownSignalException) {
            ShutdownSignalException sig = (ShutdownSignalException) reply;
            ShutdownSignalException wrapper = new ShutdownSignalException(
                    sig.isHardError(), sig.isInitiatedByApplication(),
                    sig.getReason(), sig.getReference());
            wrapper.initCause(sig);
            throw wrapper;
        } else {
            return (String) reply;
        }
    }

    public void close() throws IOException {
        connection.close();
    }

    protected DefaultConsumer setupConsumer() throws IOException {
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleShutdownSignal(String consumerTag,
                                             ShutdownSignalException signal) {
                synchronized (_contibuationMap) {
                    for (Entry<String, BlockingCell<Object>> entry : _contibuationMap
                            .entrySet()) {
                        entry.getValue().set(signal);
                    }
                    _consumer = null;
                }
            }

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                synchronized (_contibuationMap) {
                    String replyId = properties.getCorrelationId();
                    BlockingCell<Object> blocker = _contibuationMap
                            .get(replyId);
                    _contibuationMap.remove(replyId);

                    if (null != body) {
                        String replyBoay = new String(body, "UTF-8");
                        blocker.set(replyBoay);
                    } else {
                        blocker.set(body);
                    }
                }
            }
        };
        channel.basicConsume(replyQueueName, true, consumer);
        return consumer;
    }

    public static void main(String[] args) throws Exception {
        RPCClient fibrpc = new RPCClient();
        System.out.println("request fib(30)");
        String response = fibrpc.call("30");
        System.out.println("got " + response + ",");
        fibrpc.close();
    }
}
