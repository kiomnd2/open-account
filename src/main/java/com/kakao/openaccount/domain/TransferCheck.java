package com.kakao.openaccount.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class TransferCheck {


    @Id
    @GeneratedValue
    private long id;

    private String userUUID;

    private String transferUUID;

    private long wordSeq;

}
