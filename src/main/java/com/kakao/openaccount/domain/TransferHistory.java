package com.kakao.openaccount.domain;

import com.kakao.openaccount.dto.RequestType;
import com.kakao.openaccount.dto.StateType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    @Column(name = "history_no", nullable = false, length = 40)
    private Long historyNo;

    @Column(name = "request_user_uuid", nullable = false, length = 40)
    private String requestUserUUID;

    @Column(name = "transfer_uuid", nullable = false, length = 40)
    private String transferUUID;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_type", nullable = false)
    private RequestType requestType;

    @Column(name = "is_error")
    private boolean error;

    @Column(name = "request_date", length = 14)
    private LocalDateTime requestDate;

    @Column(name = "response_date", length = 14)
    private LocalDateTime responseDate;

}
