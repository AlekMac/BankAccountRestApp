package com.bank.account.exception;

public class ConcurrentUpdateException extends AppException {

    public ConcurrentUpdateException(String message) {
        super(message);
    }
}
