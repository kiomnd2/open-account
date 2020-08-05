package com.kakao.openaccount.service;

import com.kakao.openaccount.dto.CacheDTO;
import com.kakao.openaccount.dto.StateType;
import com.kakao.openaccount.dto.TransferRequestDTO;
import com.kakao.openaccount.dto.TransferResultDTO;
import com.kakao.openaccount.repository.TransferHistoryRepository;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class TransferRequestService {


    private final WordService wordService;

    private final CacheService cacheService;


    private final TransferHistoryRepository transferHistoryRepository;

    // 요청
    public TransferResultDTO requestTransfer(TransferRequestDTO requestDTO) {
        long randomSequence = wordService.findRandomSequence();

        // 먼저 해당 캐시 데이터가 있는지 확인
        if(!cacheService.hasCached(requestDTO.getTransferUUID())) {
            CacheDTO cache = cacheService.getCache(requestDTO.getTransferUUID());
            StateType type = cache.getType();
            // 타입에 문제가 없을 경우에만 요청함
            if(!type.equals(StateType.EXPIRED) || !type.equals(StateType.ERROR_NOT_SEND)) {
                // 중복이체 안되도록
            }
        }
        // 중복 요청이 아닐 경우에만
        saveCache(requestDTO);
        // 이체요청


        TransferResultDTO result = TransferResultDTO.builder()
                .requestUserUUID(requestDTO.getRequestUserUUID())
                .build();

        return result;
    }
    private void saveCache(TransferRequestDTO requestDTO) {
        CacheDTO cache = CacheDTO.builder()
                .cachingDate(LocalDateTime.now())
                .type(StateType.NORMAL)
                .requestUserUUID(requestDTO.getRequestUserUUID())
                .transferUUID(requestDTO.getTransferUUID()).build();

        cacheService.addCache(requestDTO.getTransferUUID(), cache);
    }


}
