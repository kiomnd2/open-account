package com.kakao.openaccount.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.SEQUENCE;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Word {

    @GeneratedValue(strategy = SEQUENCE)
    @Id
    private long seq;
    private String wordName;
}
