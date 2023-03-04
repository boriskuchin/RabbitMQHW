package ru.bvkuchin.rabbitmq.consumer;

import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MqComsumer {

    private static final String EXCHANGE_NAME = "itBlog";
    private static ConnectionFactory connectionFactory;
    private static Connection connection;
    private static Channel channel;

    public static void main(String[] args) throws Exception {

        if ((args.length != 2) || !args[0].equals("set_topic")) {
            throw new RuntimeException("Incorrect set_topic command");
        }


        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connection = connectionFactory.newConnection();
        channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);



        String queueName = channel.queueDeclare().getQueue();
        System.out.println("My queue name: " + queueName);

        String topic = args[1];

        channel.queueBind(queueName, EXCHANGE_NAME, topic);

        System.out.println(" [*] Waiting for messages " + topic);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }
}
