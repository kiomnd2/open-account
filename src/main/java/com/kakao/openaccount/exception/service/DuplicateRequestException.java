package com.kakao.openaccount.exception.service;

import javax.validation.ValidationException;

public class DuplicateRequestException extends RuntimeException {

    public DuplicateRequestException() {
        super("중복이체가 확인되었습니다.");
    }
}