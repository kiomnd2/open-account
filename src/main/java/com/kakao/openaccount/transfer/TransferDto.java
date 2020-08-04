package com.kakao.openaccount.transfer;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@NoArgsConstructor
public class TransferDto {
    private String enterpriseCode;
    private String accountNo;
}

