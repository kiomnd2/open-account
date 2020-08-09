package com.kakao.openaccount.service;

import com.kakao.openaccount.domain.TransferCheck;
import com.kakao.openaccount.domain.TransferHistory;
import com.kakao.openaccount.dto.*;
import com.kakao.openaccount.exception.service.DuplicateRequestException;
import com.kakao.openaccount.repository.CheckWordRepository;
import com.kakao.openaccount.repository.TransferHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TransferRequestService {


    private final WordService wordService;

    private final CacheService cacheService;

    private final TransferHistoryRepository transferHistoryRepository;

    private final CheckWordRepository checkWordRepository;

    private final  AsyncQueue asyncQueue;


    // 요청
    public TransferResultDTO requestTransfer(@Valid TransferRequestDTO requestDTO) {

        // 재요청일 경우에..
        TransferCheck byTransferUUID = checkWordRepository.findByTransferUUID(requestDTO.getTransferUUID());
        if(byTransferUUID != null ) {
            StateType stateType = byTransferUUID.getStateType();
            // 재시도 시, 오류임에도 인데 1원이체에 성공했을 경우를 제외하고 다시 시도 하지 않는다
            if (stateType !=StateType.ERROR_SEND && stateType != StateType.SUCCESS) { // or !Expired
                System.out.println(stateType);
                throw new DuplicateRequestException();
            }
        }
        // 중복요청인지 확인
        if(cacheService.hasCached(requestDTO.getTransferUUID())) {
            // 만약 중복 거래라면, 익셉션 처리
            throw new DuplicateRequestException();
        }
        cacheService.saveCache(requestDTO);

        // 랜덤단어 출력
        long randomSequence = wordService.findRandomSequence();

        // 최초 거래 트렌젝션 추가
        TransferCheck transaction = saveTransferCheckData(requestDTO, randomSequence);

        // 이체 요청

        TransferResultDTO result = request(requestDTO);

        // 거래정보 갱신
        checkWordRepository.save(transaction.updateStateType(StateType.SUCCESS));

        return result;
    }

    public String findTransactionUUID(RequestUser requestUser) {
        List<TransferCheck> checkList = this.checkWordRepository.findByUserUUID(requestUser.getRequestUserUUID());
        TransferCheck transferCheck = checkList
                .stream().filter(f -> (f.getStateType() != null && f.getStateType() != StateType.SUCCESS)).findFirst().orElse(null);
        if ( transferCheck != null ) {
            return transferCheck.getTransferUUID();
        }

        return null;
    }

    public TransferCheck saveTransferCheckData(TransferRequestDTO requestDTO, long randomSequence) {
        return this.checkWordRepository.save(TransferCheck.builder()
                .transferUUID(requestDTO.getTransferUUID())
                .userUUID(requestDTO.getRequestUserUUID())
                .wordSeq(randomSequence)
                .stateType(StateType.NORMAL)
                .build());
    }

    public TransferResultDTO request(@Valid TransferRequestDTO transferRequestDTO) {

        TransferResultDTO resultDTO = asyncQueue.addRequest(transferRequestDTO);
        if (resultDTO.isError()) {
            // 1원이체에 성공했는지 확인

            // 조회 이력 쌓기
            saveHistory(resultDTO, RequestType.TRANSFER_INSERT);

            transferRequestDTO.updateRequestType(RequestType.TRANSFER_SEARCH);
            TransferResultDTO searchResult = asyncQueue.addRequest(transferRequestDTO);

            cacheService.removeCache(transferRequestDTO.getTransferUUID()); // 캐시 제거

            if (!searchResult.isError()) { // 조회햇는데 정상이 확인되면
                resultDTO.updateMessage(searchResult.getMessage());
                return resultDTO;
            } else {
                return requestTransfer(transferRequestDTO); // 재요청
            }
        }

        // 캐시제거
        cacheService.removeCache(transferRequestDTO.getTransferUUID());
        //이력 쌓기
        saveHistory(resultDTO, RequestType.TRANSFER_INSERT);


        return resultDTO;
    }


    private void saveHistory(@Valid TransferResultDTO resultDTO, RequestType resultType) {
        String uuid = UUID.randomUUID().toString();
        TransferHistory history = TransferHistory.builder()
                .userUUID(resultDTO.getRequestUserUUID())
                .historyNo(uuid)
                .requestDate(resultDTO.getRequestDate())
                .responseDate(resultDTO.getResponseDate())
                .requestType(resultType)
                .error(resultDTO.isError())
                .build();

        transferHistoryRepository.save(history);
    }


}
