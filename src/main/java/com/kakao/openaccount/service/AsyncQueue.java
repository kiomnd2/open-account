package com.kakao.openaccount.service;

import com.kakao.openaccount.dto.TransferRequestDTO;
import com.kakao.openaccount.dto.TransferResultDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedQueue;

@RequiredArgsConstructor
@Component
@Getter
public class AsyncQueue {


    private final EventProcessor eventProcessor;

    ConcurrentLinkedQueue<TransferRequestDTO> requestQ = new ConcurrentLinkedQueue<>();

    public TransferResultDTO addRequest(TransferRequestDTO requestDTO) {
        this.requestQ.add(requestDTO);
        return this.eventProcessor.execute(this);

    }
}
