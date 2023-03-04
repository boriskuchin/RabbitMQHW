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

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connection = connectionFactory.newConnection();


        while (true) {
            String[] command = bufferedReader.readLine().split(" ");


            if ((command.length == 1) && command[0].equals("unsubscribe")) {
                if (channel.isOpen()) {
                    channel.close();
                    System.out.println("Unsubscribed");
                }
            }

            if ((command.length == 2) && command[0].equals("set_topic")) {

                channel = connection.createChannel();
                channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

                String queueName = channel.queueDeclare().getQueue();
                System.out.println("My queue name: " + queueName);

                String topic = command[1];

                channel.queueBind(queueName, EXCHANGE_NAME, topic);

                System.out.println(" [*] Waiting for messages " + topic);

                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), "UTF-8");
                    System.out.println(" [x] Received '" + message + "'");
                };
                channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
            }
        }
    }
}
