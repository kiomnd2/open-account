package com.kakao.openaccount.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferHistoryDTO {

    @NotNull
    private Long historyNo;

    @NotNull
    private String requestUserUUID;

    private RequestType requestType;

    private StateType stateType;

    private LocalDateTime requestDate;

    private LocalDateTime responseDate;
}
