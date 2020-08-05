package com.kakao.openaccount.domain;

import com.kakao.openaccount.dto.RequestType;
import com.kakao.openaccount.dto.StateType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class TransferHistory {

    @Id @GeneratedValue
    private long id;

    private Long historyNo;

    private String requestUserUUID;

    private String transferUUID;

    private String enterpriseCode;

    private String accountNo;

    private RequestType requestType;

    private StateType stateType;

    private LocalDateTime requestDate;

    private LocalDateTime responseDate;

}
