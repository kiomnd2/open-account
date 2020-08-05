package com.kakao.openaccount.exception.service;

import javax.persistence.EntityNotFoundException;

public class NotFoundWordException extends EntityNotFoundException {
    public NotFoundWordException() {
    }

    public NotFoundWordException(String message) {
        super(message);
    }
}
