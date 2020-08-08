package com.kakao.openaccount.domain;


import com.kakao.openaccount.dto.StateType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "transfer_check")
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

    public TransferCheck updateStateType(StateType stateType) {
        this.stateType = stateType;
        return this;
    }


}
