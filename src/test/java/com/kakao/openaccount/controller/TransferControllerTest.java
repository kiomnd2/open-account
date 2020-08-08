package com.kakao.openaccount.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.openaccount.domain.TransferCheck;
import com.kakao.openaccount.domain.TransferHistory;
import com.kakao.openaccount.dto.RequestType;
import com.kakao.openaccount.dto.RequestUser;
import com.kakao.openaccount.dto.StateType;
import com.kakao.openaccount.dto.TransferRequestDTO;
import com.kakao.openaccount.exception.service.DuplicateRequestException;
import com.kakao.openaccount.repository.CheckWordRepository;
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
import org.springframework.context.annotation.Description;
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
    CheckWordRepository checkWordRepository;

    @Autowired
    TransferHistoryRepository transferHistoryRepository;


    @AfterEach
    public void afterEach() {
        this.cacheService.clear();
    }

    @Description("계좌번호 12345 일때요청->성공")
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


    @Description("계좌번호 123이 아닐때 요청 ->실패")
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

    @Description("요청 후, 캐시사이즈 확인")
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


        int cacheSize = cacheService.getCacheSize();

        assertThat(cacheSize).isZero(); // 성공후 잘 지워지는지
    }


    @Description("1원 중복이체시 익셉션 처리 확인")
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

    @Description("재요청에 대한 처리")
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



    @Description("정상적으로  정상이력이 저장되었는지 확인")
    @Test
    public void requestTransferAndCheckSuccessHistory() {
        String userUUID = UUID.randomUUID().toString();
        String transferUUID = UUID.randomUUID().toString();


        LocalDateTime now = LocalDateTime.now();
        TransferRequestDTO requestDTO = TransferRequestDTO.builder()
                .requestDate(now)
                .requestType(RequestType.TRANSFER_INSERT)
                .requestUserUUID(userUUID)
                .accountNo("123456789")
                .bankCode("024")
                .transferUUID(transferUUID).build();

        transferRequestService.requestTransfer(requestDTO);

        // 히스토리가 확인되는지 체크
        TransferHistory history = transferHistoryRepository.findByUserUUID(userUUID);

        assertNotNull(history);
        assertThat(history.getRequestDate()).isAfterOrEqualTo(now);
        assertThat(history.getUserUUID()).isEqualTo(userUUID);
        assertThat(history.isError()).isFalse();
    }


    @Description("정상적으로 실패 일겨이 남았는지 확인")
    @Test
    public void requestTransferAndCheckFailHistory() {
        String userUUID = UUID.randomUUID().toString();
        String transferUUID = UUID.randomUUID().toString();


        LocalDateTime now = LocalDateTime.now();
        TransferRequestDTO requestDTO = TransferRequestDTO.builder()
                .requestDate(now)
                .requestType(RequestType.TRANSFER_INSERT)
                .requestUserUUID(userUUID)
                .accountNo("56456789")
                .bankCode("024")
                .transferUUID(transferUUID).build();

        transferRequestService.requestTransfer(requestDTO);

        // 히스토리가 확인되는지 체크
        TransferHistory history = transferHistoryRepository.findByUserUUID(userUUID);

        assertNotNull(history);
        assertThat(history.getRequestDate()).isAfterOrEqualTo(now);
        assertThat(history.getUserUUID()).isEqualTo(userUUID);
        assertThat(history.isError()).isTrue();
    }



    @Description("성공적인 이체후, 트렌젝션이 변경 되었는지 확인")
    @Test
    public void requestTransferAndCheckTransaction() {
        String userUUID = UUID.randomUUID().toString();
        String transferUUID = UUID.randomUUID().toString();


        LocalDateTime now = LocalDateTime.now();
        TransferRequestDTO requestDTO = TransferRequestDTO.builder()
                .requestDate(now)
                .requestType(RequestType.TRANSFER_INSERT)
                .requestUserUUID(userUUID)
                .accountNo("123456789")
                .bankCode("024")
                .transferUUID(transferUUID).build();

        transferRequestService.requestTransfer(requestDTO);

        TransferCheck byUserUUIDAndTransferUUID = checkWordRepository.findByUserUUIDAndTransferUUID(userUUID, transferUUID);

        assertThat(byUserUUIDAndTransferUUID.getStateType()).isEqualTo(StateType.SUCCESS);

    }




}
