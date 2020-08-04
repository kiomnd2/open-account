package com.kakao.openaccount.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class History {

    @Id @GeneratedValue
    private long id;

    private String userUUID;

    private String stageCode;

    private LocalDateTime certificationDate;

    private String resultCode;



}
