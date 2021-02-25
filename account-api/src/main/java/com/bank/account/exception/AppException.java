package com.bank.account.exception;

public abstract class AppException extends RuntimeException{

    AppException(String message){
        super(message);
    }
}
