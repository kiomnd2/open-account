package com.kakao.openaccount.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestUser {
    
    private String requestUserUUID;

    private String userId;

    private String userName;

}
