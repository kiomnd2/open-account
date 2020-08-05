package com.kakao.openaccount.transfer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class Receiver {


    @RabbitListener(queues = "ReceiveQ")
    public void processMessage() {

    }
}
