package com.kakao.openaccount.exception.service;

import javax.persistence.EntityNotFoundException;

public class NotFoundWordException extends RuntimeException {
    public NotFoundWordException() {
        super("단어정볼를 찾을 수 없습니다.");
    }
}
