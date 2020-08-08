package com.kakao.openaccount.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEvent;

import javax.validation.constraints.Max;
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
    @Max(20)
    private String accountNo;

    private RequestType requestType;

    private LocalDateTime requestDate;

    public void updateRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

}
