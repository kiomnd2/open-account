package com.kakao.openaccount.exception.service;

public class DuplicateRequestException extends RuntimeException{

    public DuplicateRequestException() {
    }

    public DuplicateRequestException(String message) {
        super(message);
    }
}
