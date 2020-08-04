package com.kakao.openaccount.transfer;

import com.kakao.openaccount.history.HistoryService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Receiver {

    final HistoryService historyService;

    public Receiver(HistoryService historyService) {
        this.historyService = historyService;
    }

    @RabbitListener(queues = "ReceiveQ")
    public void processMessage() {

    }
}
