package com.kakao.openaccount.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class TransferResultDTO {

    @NotNull
    private String requestUserUUID;

    @NotNull
    private String transferUUID;

    @NotNull
    private LocalDateTime requestDate;

    @NotNull
    private LocalDateTime responseDate;

    @NotNull
    private boolean error;

    private String message;
}
