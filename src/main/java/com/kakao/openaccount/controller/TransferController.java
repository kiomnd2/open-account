package com.kakao.openaccount.controller;

import com.kakao.openaccount.dto.RequestType;
import com.kakao.openaccount.dto.RequestUser;
import com.kakao.openaccount.dto.TransferRequestDTO;
import com.kakao.openaccount.dto.TransferResultDTO;
import com.kakao.openaccount.service.TransferRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor

public class TransferController {


    private final TransferRequestService transferRequestService;

    @PostMapping(value ="/api/transfer-auth", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity certificationByTransfer(@RequestBody @Valid RequestUser requestUser, Errors error) {

        if(error.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }


        // 거래 코드 임시 생성
        String transactionUUID = transferRequestService.findTransactionUUID(requestUser);
        if(transactionUUID == null) {
            transactionUUID = UUID.randomUUID().toString();
        }

        TransferRequestDTO transferRequester = TransferRequestDTO.builder()
                .requestUserUUID(requestUser.getRequestUserUUID())
                .transferUUID(transactionUUID)
                .accountNo(requestUser.getAccountNo())
                .bankCode(requestUser.getBankCode())
                .requestDate(LocalDateTime.now())
                .requestType(RequestType.TRANSFER_INSERT).build();

        // 요청..
        TransferResultDTO transferResult = transferRequestService.requestTransfer(transferRequester);


        if(transferResult.isError()) {
            return ResponseEntity.badRequest().body(transferResult); //
        }

        return ResponseEntity.ok().body(transferResult);
    }
}
