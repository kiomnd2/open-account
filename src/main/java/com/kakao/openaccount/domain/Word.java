package com.kakao.openaccount.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "words")
public class Word {

    @GeneratedValue(strategy = SEQUENCE)
    @Id
    @Column(name = "word_seq")
    private long seq;

    @Column(name = "word_name", nullable = false, length = 4)
    private String wordName;


}
