package com.kakao.openaccount.controller;

import com.kakao.openaccount.dto.RequestType;
import com.kakao.openaccount.dto.RequestUser;
import com.kakao.openaccount.dto.TransferRequestDTO;
import com.kakao.openaccount.dto.TransferResultDTO;
import com.kakao.openaccount.service.TransferRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transfer-auth")
public class TransferController {


    final TransferRequestService transferRequestService;

    @PostMapping(value ="{enterpriseCode}/{accountNo}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity certificationByTransfer(@RequestBody @Valid RequestUser requestUser, @PathVariable String enterpriseCode, @PathVariable String accountNo, Errors error ) {

        if(error.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        // 거래 코드 임시 생성
        UUID transferUUID = UUID.randomUUID();

        TransferRequestDTO transferRequester = TransferRequestDTO.builder()
                .requestUserUUID(requestUser.getRequestUserUUID())
                .transferUUID(transferUUID.toString())
                .accountNo(accountNo)
                .enterpriseCode(enterpriseCode)
                .requestType(RequestType.TRANSFER).build();

        // 요청..
        TransferResultDTO transferResult = transferRequestService.requestTransfer(transferRequester);


        return ResponseEntity.ok().build();
    }
}
