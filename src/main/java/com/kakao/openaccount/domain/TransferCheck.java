package com.kakao.openaccount.domain;


import com.kakao.openaccount.dto.StateType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
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

    @Column( name = "user_uuid", nullable = false, length = 40)
    private String userUUID;

    @Column( name = "transfer_uuid", nullable = false, length = 40)
    private String transferUUID;

    private long wordSeq;
    
    private StateType stateType;

}
