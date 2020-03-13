package com.xuecheng.test.rabbitmq.simple;

import com.rabbitmq.client.*;
import com.xuecheng.test.rabbitmq.util.ConnectionUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by BinXU on 2020/01/30.
 */
public class Consumer {
    //队列名称
    static final String QUEUE_NAME = "simple_queue";

    public static void main(String[] args) throws Exception {
        //获取连接
        Connection connection = ConnectionUtil.getConnection();
        //创建频道
        Channel channel = connection.createChannel();
        // 声明（创建）队列
        /**
         * 参数1：队列名称
         * 参数2：是否定义持久化队列
         * 参数3：是否独占本次连接
         * 参数4：是否在不使用的时候自动删除队列
         * * 参数5：队列其它参数
         * */
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        //创建消费者；并设置消息处理
        DefaultConsumer consumer = new DefaultConsumer(channel){
            @Override
        /**
         * consumerTag 消息者标签，在channel.basicConsume时候可以指定
         * envelope 消息包的内容，可从中获取消息id，消息routingkey，交换机，消息和重传标志
         (收到消息失败后是否需要重新发送)
         * properties 属性信息
         * body 消息
         */
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException, UnsupportedEncodingException {
                //路由key
                System.out.println("路由key为：" + envelope.getRoutingKey());
                //交换机
                System.out.println("交换机为：" + envelope.getExchange());
                //消息id
                System.out.println("消息id为：" + envelope.getDeliveryTag());
                //收到的消息
                System.out.println("接收到的消息为：" + new String(body, "utf-8"));
            }
        };
        //监听消息
        /**
         * 参数1：队列名称
         * 参数2：是否自动确认，设置为true为表示消息接收到自动向mq回复接收到了，mq接收到回复会删除消
         息，设置为false则需要手动确认
         * 参数3：消息接收到后回调
         */
        channel.basicConsume(QUEUE_NAME, true, consumer);
        //不关闭资源，应该一直监听消息
        //channel.close();
        //connection.close();
    }
}
