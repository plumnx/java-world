package com.plumnix.cloud;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Random;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @RestController
    public static class Index {

        @Autowired
        private RocketMQTemplate rocketMQTemplate;

        @GetMapping
        public void sendMessage() {
            rocketMQTemplate.syncSend("test", Message.builder().content("This is a test message.").id(new Random().nextInt(10)).date(new Date()).build().toString());
        }

    }

    @Component
    @RocketMQMessageListener(
            consumeThreadMax = 1,
            consumerGroup = "consumer-demo-1",
            topic = "test"
    )
    public static class MessageConsumer implements RocketMQListener<String> {

        @Override
        public void onMessage(String s) {
            System.out.println("Receiving message: " + s);
        }

    }

}
