package ru.bvkuchin.rabbitmq.producer;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MqProducer {
    private final static String EXCHANGER_NAME = "itBlog";

    public static void main(String[] args) throws Exception {

        BufferedReader buffReader = new BufferedReader(new InputStreamReader(System.in));

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGER_NAME, BuiltinExchangeType.DIRECT);


            while (true) {
                String message = buffReader.readLine();

                System.out.println("------***************------");
                String header = message.split(" ", 2)[0];
                System.out.println("header: " + header);
                String body = message.split(" ", 2)[1];
                System.out.println("body: " + body);
                System.out.println("------***************------");

                channel.basicPublish(EXCHANGER_NAME, header, null, body.getBytes());
                System.out.println(" [x] Sent '" + message + "'");
            }
        }
    }
}

// Домашнее задание:
// 1. Сделайте два консольных приложения (не Спринг):
//
//   а. IT-блог, который публикует статьи по языкам программирования
//   б. Подписчик, которого интересуют статьи по определенным языкам
//
//   Детали a. Если IT-блог в консоли пишет 'php some message', то 'some message'
//   отправляется в RabbitMQ с темой 'php', и это сообщение получают подписчики
//   этой темы
//
//   Детали b. Подписчик при запуске должен ввести команду 'set_topic php', после
//   чего начнет получать сообщения из очереди с соответствующей темой 'php'
//
// 2. * Сделайте возможность клиентов подписываться и отписываться от статей по темам
// в процессе работы приложения-подписчика


