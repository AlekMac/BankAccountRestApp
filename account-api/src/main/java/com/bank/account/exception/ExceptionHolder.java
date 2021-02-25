package com.bank.account.exception;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ExceptionHolder {

    private final String message;
    private final LocalDateTime timestamp;

}
