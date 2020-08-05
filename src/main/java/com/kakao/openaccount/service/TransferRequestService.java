package com.kakao.openaccount.service;

import com.kakao.openaccount.dto.TransferRequestDTO;
import com.kakao.openaccount.dto.TransferResultDTO;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@RequiredArgsConstructor
public class TransferRequestService {


    WordService wordService;

    @Autowired
    public TransferRequestService(WordService wordService) {
        this.wordService = wordService;
    }


    public TransferResultDTO requestTransfer(TransferRequestDTO requestDTO) {
        String randomWord = wordService.findRandomWord();
    }

    public void saveTempCertificationData(TransferRequestDTO requestDTO) {
        // memCache 저장
    }


    public void getTempCertificationData(TransferRequestDTO requestDTO) {
        // memCache 가져오기


    }

}
