package com.kakao.openaccount.service;

import com.kakao.openaccount.dto.TransferRequestDTO;
import com.kakao.openaccount.dto.TransferResultDTO;
import com.kakao.openaccount.word.WordRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@AllArgsConstructor

public class TransferRequestService {

    WordRepository wordRepository;

    public TransferResultDTO requestTransfer(TransferRequestDTO requestDTO) {

    }

    public void saveTempCertificationData(TransferRequestDTO requestDTO) {
        // memCache 저장
    }


    public void getTempCertificationData(TransferRequestDTO requestDTO) {
        // memCache 가져오기
        

    }

}
