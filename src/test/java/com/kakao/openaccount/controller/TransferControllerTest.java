package com.kakao.openaccount.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.openaccount.domain.TransferHistory;
import com.kakao.openaccount.dto.RequestType;
import com.kakao.openaccount.dto.RequestUser;
import com.kakao.openaccount.dto.TransferRequestDTO;
import com.kakao.openaccount.exception.service.DuplicateRequestException;
import com.kakao.openaccount.repository.TransferHistoryRepository;
import com.kakao.openaccount.service.CacheService;
import com.kakao.openaccount.service.TransferRequestService;
import com.kakao.openaccount.service.WordService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
class TransferControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CacheService cacheService;

    @Autowired
    WordService wordService;

    @Autowired
    TransferRequestService transferRequestService;

    @Autowired
    TransferHistoryRepository transferHistoryRepository;


    @AfterEach
    public void afterEach() {
        this.cacheService.clear();
    }

    @Test
    public void requestTransfer_success() throws Exception {
        String userUUID = UUID.randomUUID().toString();
        RequestUser requestUser = RequestUser.builder()
                .requestUserUUID(userUUID)
                .userId("kiomnd2")
                .userName("홍길동")
                .accountNo("1234567890")
                .bankCode("042")
                .build();
        mockMvc.perform(post("/api/transfer-auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(requestUser)))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }


    @Test
    public void requestTransfer_fail() throws Exception {

        String userUUID = UUID.randomUUID().toString();
        RequestUser requestUser = RequestUser.builder()
                .requestUserUUID(userUUID)
                .userId("kiomnd2")
                .userName("홍길동")
                .accountNo("5677890")
                .bankCode("042")
                .build();
        mockMvc.perform(post("/api/transfer-auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(requestUser)))
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

    }

    @Test
    public void requestTransferAndConfirmCacheSize() throws Exception {
        String userUUID = UUID.randomUUID().toString();
        RequestUser requestUser = RequestUser.builder()
                .requestUserUUID(userUUID)
                .userId("kiomnd2")
                .userName("홍길동")
                .accountNo("1234567890")
                .bankCode("042")
                .build();
        mockMvc.perform(post("/api/transfer-auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(requestUser)))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));


        String transactionUUID = transferRequestService.findTransactionUUID(requestUser);
        int cacheSize = cacheService.getCacheSize();

        assertNotNull(transactionUUID);
        assertThat(cacheSize).isZero(); // 성공후 잘 지워지는지
    }


    @Test
    public void requestTransferAndDuplicateTransferCheckNotIncludeDatabase() throws Exception{
        String userUUID = UUID.randomUUID().toString();
        String transferUUID = UUID.randomUUID().toString();


        TransferRequestDTO requestDTO = TransferRequestDTO.builder()
                .requestDate(LocalDateTime.now())
                .requestType(RequestType.TRANSFER_INSERT)
                .requestUserUUID(userUUID)
                .accountNo("123456789")
                .bankCode("024")
                .transferUUID(transferUUID).build();

        cacheService.saveCache(requestDTO);
        // 중복 요청
        assertThrows(DuplicateRequestException.class, ()-> transferRequestService.requestTransfer(requestDTO) );

    }

    @Test
    public void requestTransferAndDuplicateTransferCheck() {
        String userUUID = UUID.randomUUID().toString();
        String transferUUID = UUID.randomUUID().toString();
        long randomSequence = wordService.findRandomSequence();


        TransferRequestDTO requestDTO = TransferRequestDTO.builder()
                .requestDate(LocalDateTime.now())
                .requestType(RequestType.TRANSFER_INSERT)
                .requestUserUUID(userUUID)
                .accountNo("123456789")
                .bankCode("024")
                .transferUUID(transferUUID).build();

        cacheService.saveCache(requestDTO);

        transferRequestService.saveTransferCheckData(requestDTO, randomSequence);

        assertThrows(DuplicateRequestException.class, ()-> transferRequestService.requestTransfer(requestDTO) );
    }



    @Test
    public void requestTransferAndCheckSuccessHistory() {
        String userUUID = UUID.randomUUID().toString();
        String transferUUID = UUID.randomUUID().toString();


        TransferRequestDTO requestDTO = TransferRequestDTO.builder()
                .requestDate(LocalDateTime.now())
                .requestType(RequestType.TRANSFER_INSERT)
                .requestUserUUID(userUUID)
                .accountNo("123456789")
                .bankCode("024")
                .transferUUID(transferUUID).build();

        transferRequestService.requestTransfer(requestDTO);

        // 히스토리가 확인되는지 체크
        TransferHistory history = transferHistoryRepository.findByUserUUIDAndTransferUUID(userUUID, transferUUID);

        assertNotNull(history);
        assertThat(history.isError()).isFalse();
    }


    @Test
    public void requestTransferAndCheckFailHistory() {
        String userUUID = UUID.randomUUID().toString();
        String transferUUID = UUID.randomUUID().toString();
        long randomSequence = wordService.findRandomSequence();


        TransferRequestDTO requestDTO = TransferRequestDTO.builder()
                .requestDate(LocalDateTime.now())
                .requestType(RequestType.TRANSFER_INSERT)
                .requestUserUUID(userUUID)
                .accountNo("56456789")
                .bankCode("024")
                .transferUUID(transferUUID).build();

        transferRequestService.requestTransfer(requestDTO);

        // 히스토리가 확인되는지 체크
        TransferHistory history = transferHistoryRepository.findByUserUUIDAndTransferUUID(userUUID, transferUUID);

        assertNotNull(history);
        assertThat(history.isError()).isTrue();
    }



}
