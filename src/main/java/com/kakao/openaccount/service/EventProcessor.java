package com.kakao.openaccount.service;

import com.kakao.openaccount.dto.RequestType;
import com.kakao.openaccount.dto.ResultType;
import com.kakao.openaccount.dto.TransferRequestDTO;
import com.kakao.openaccount.dto.TransferResultDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventProcessor {


    public TransferResultDTO execute(AsyncQueue asyncQueue) {
        ConcurrentLinkedQueue<TransferRequestDTO> requestQ = asyncQueue.getRequestQ();
        TransferResultDTO resultDTO = null;
        if(!requestQ.isEmpty()) {

            //조회요청인지 등록요청인지..
            TransferRequestDTO requestDTO = requestQ.poll();

            if(requestDTO.getRequestType() == RequestType.TRANSFER_INSERT)
            {
                // 12345 로 시작하는 계좌번호는 무조건 성공
                String accountNo = requestDTO.getAccountNo();
                if (accountNo.startsWith("12345")) {
                    resultDTO = TransferResultDTO.builder()
                            .transferUUID(requestDTO.getTransferUUID())
                            .requestUserUUID(requestDTO.getRequestUserUUID())
                            .requestDate(requestDTO.getRequestDate())
                            .responseDate(LocalDateTime.now())
                            .error(false)
                            .resultType(ResultType.SUCCESS)
                            .message("이체에 성공하였습니다")
                            .build();
                }


                // 567 로 시작하는 계좌번호는 무조건 실패
                else  {
                    resultDTO = TransferResultDTO.builder()
                            .transferUUID(requestDTO.getTransferUUID())
                            .requestUserUUID(requestDTO.getRequestUserUUID())
                            .requestDate(requestDTO.getRequestDate())
                            .responseDate(LocalDateTime.now())
                            .error(true)
                            .resultType(ResultType.FAIL)
                            .message("이체에 실패하였습니다")
                            .build();
                }
            }
            else { // 리퀘스트 타입이 조회일 경우
                // 조회는 그냥 성공시키자..
                resultDTO = TransferResultDTO.builder()
                        .transferUUID(requestDTO.getTransferUUID())
                        .requestUserUUID(requestDTO.getRequestUserUUID())
                        .requestDate(requestDTO.getRequestDate())
                        .responseDate(LocalDateTime.now())
                        .error(false)
                        .resultType(ResultType.SUCCESS)
                        .message("1원 이체 성공을 확인했습니다.")
                        .build();
            }
        }
        return resultDTO;
    }


}
