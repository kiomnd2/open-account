package com.kakao.openaccount.transfer;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class Sender {

    final RabbitMessagingTemplate template;

    public Sender(RabbitMessagingTemplate template) {
        this.template = template;
    }

    @Bean
    Queue queue() {
        return new Queue("TransferQ", false);
    }
    @Bean
    Queue queue1() {
        return new Queue("ReceiveQ", false);
    }

    public void send(Object message) {
        template.convertAndSend("TransferQ", message);
    }

}
