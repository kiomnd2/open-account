package com.kakao.openaccount.domain;

import com.kakao.openaccount.dto.RequestType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cert_history")
public class TransferHistory {

    @Id @GeneratedValue
    private long id;

    @Column(name = "history_no", nullable = false, length = 40)
    private String historyNo;

    @Column(name = "user_uuid", nullable = false, length = 40)
    private String userUUID;

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
