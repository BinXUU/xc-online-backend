package com.xuecheng.test.rabbitmq.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * ConnectionUtil工具类，用于获取RabbitMQ Connection连接
 */
public class ConnectionUtil {
    public static Connection getConnection() throws Exception {
        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //主机地址;默认为 localhost
        connectionFactory.setHost("localhost");
        //连接端口;默认为 5672
        connectionFactory.setPort(5672);
        //虚拟主机名称;默认为 /
        connectionFactory.setVirtualHost("/demo");
        //连接用户名；默认为guest
        connectionFactory.setUsername("demo");
        //连接密码；默认为guest
        connectionFactory.setPassword("demo");
        //创建连接
        return connectionFactory.newConnection();
    }

}
