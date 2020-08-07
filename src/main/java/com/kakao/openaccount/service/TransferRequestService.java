package com.kakao.openaccount.service;

import com.kakao.openaccount.domain.TransferCheck;
import com.kakao.openaccount.dto.RequestUser;
import com.kakao.openaccount.dto.StateType;
import com.kakao.openaccount.dto.TransferRequestDTO;
import com.kakao.openaccount.dto.TransferResultDTO;
import com.kakao.openaccount.exception.service.DuplicateRequestException;
import com.kakao.openaccount.netty.ConnectionManager;
import com.kakao.openaccount.repository.CheckWordRepository;
import com.kakao.openaccount.repository.TransferHistoryRepository;
import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TransferRequestService {


    private final WordService wordService;

    private final CacheService cacheService;

    private final ConnectionManager connectionManager;

    private final TransferHistoryRepository transferHistoryRepository;

    private final CheckWordRepository checkWordRepository;

    // 요청
    public TransferResultDTO requestTransfer(TransferRequestDTO requestDTO) {
        long randomSequence = wordService.findRandomSequence();


        // 중복요청인지 확인
        if(cacheService.hasCached(requestDTO.getTransferUUID())) {
            Optional<TransferCheck> byTransferUUID = checkWordRepository.findByTransferUUID(requestDTO.getTransferUUID());
            // 현재 캐시에는 데이ㅌ가 있는데
            TransferCheck transferCheck = byTransferUUID.orElseThrow(EntityNotFoundException::new);
            StateType stateType = transferCheck.getStateType();
            // 중복요청중, 실제 이체에 실패했을 경우에는 다시 요청을 보내준다
            if(!stateType.equals(StateType.EXPIRED) || !stateType.equals(StateType.ERROR_NOT_SEND)) {
                // 중복이체 안되도록
                throw new DuplicateRequestException();
            }
        }
        cacheService.saveCache(requestDTO);
        // DB에 정보 저정
        saveTransferCheckData(requestDTO, randomSequence);

        /*// 이체 요청
        TransferResultDTO response = call(requestDTO);
        */

        TransferResultDTO result = TransferResultDTO.builder()
                .requestUserUUID(requestDTO.getRequestUserUUID())
                .build();

        return result;
    }

    public String findTransactionUUID(RequestUser requestUser) {
        TransferCheck transferCheck = this.checkWordRepository.findByUserUUID(requestUser.getRequestUserUUID());
        if (transferCheck != null) {
            return transferCheck.getTransferUUID();
        }
        return null;
    }

    public void saveTransferCheckData(TransferRequestDTO requestDTO, long randomSequence) {
        this.checkWordRepository.save(TransferCheck.builder()
            .transferUUID(requestDTO.getTransferUUID())
            .userUUID(requestDTO.getRequestUserUUID())
            .wordSeq(randomSequence)
            .stateType(StateType.NORMAL)
        .build());
    }

    protected TransferResultDTO call(TransferRequestDTO transferRequestDTO) {
        Channel connectChannel = connectionManager.start();
        connectChannel.writeAndFlush("히히히히히히");
        // ObjectMapper;


        TransferResultDTO result = TransferResultDTO.builder()
                .transferUUID(transferRequestDTO.getTransferUUID())
                .build();

        return result;

    }


}
