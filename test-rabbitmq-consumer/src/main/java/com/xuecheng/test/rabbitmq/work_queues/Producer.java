package com.xuecheng.test.rabbitmq.work_queues;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.xuecheng.test.rabbitmq.util.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by BinXU on 2020/01/30.
 */
public class Producer {
    static final String QUEUE_NAME = "work_queue";

    public static void main(String[] args) throws Exception {
        //创建连接
        Connection connection = ConnectionUtil.getConnection();
        // 创建频道
        Channel channel = connection.createChannel();
        // 声明（创建）队列
        /**
         * 参数1：队列名称
         * 参数2：是否定义持久化队列
         * 参数3：是否独占本次连接
         * 参数4：是否在不使用的时候自动删除队列
         * 参数5：队列其它参数
         */
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        for (int i = 1; i <= 30; i++) {
        // 发送信息
            String message = "你好；小兔子！work模式--" + i;
        /**
         * 参数1：交换机名称，如果没有指定则使用默认Default Exchage
         * 参数2：路由key,简单模式可以传递队列名称
         * 参数3：消息其它属性
         * 参数4：消息内容
         */
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("已发送消息：" + message);
        }
        // 关闭资源
        channel.close();
        connection.close();

    }
}
