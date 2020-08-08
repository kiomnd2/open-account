package com.kakao.openaccount.dto;


import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestUser {

    @NotNull
    private String requestUserUUID;

    @NotNull
    private String userId;

    private String userName;

    @NotNull
    private String accountNo;

    @NotNull
    private String bankCode;

}
