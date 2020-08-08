package com.kakao.openaccount.dto;


import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@Getter
public class TransferRequestDTO {

    @NotNull
    private String requestUserUUID;

    @NotNull
    private String transferUUID;

    @NotNull
    private String bankCode;

    @NotNull
    private String accountNo;

    private RequestType requestType;

    @DateTimeFormat
    private LocalDateTime requestDate;

    public void updateRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

}
