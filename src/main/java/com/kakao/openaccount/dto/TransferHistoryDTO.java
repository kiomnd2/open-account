package com.kakao.openaccount.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferHistoryDTO {

    @NotNull
    private Long historyNo;

    @NotNull
    private String requestUserUUID;

    @NotNull
    private String transferUUID;

    @NotNull
    private String enterpriseCode;

    @NotNull
    private String accountNo;

    private RequestType requestType;

    private StateType stateType;

}
