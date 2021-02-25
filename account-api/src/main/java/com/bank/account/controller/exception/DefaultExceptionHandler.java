package com.bank.account.controller.exception;

import com.bank.account.exception.AppException;
import com.bank.account.exception.ExceptionHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<Object> handleAccountException(AppException e, WebRequest request){
        ExceptionHolder holder = ExceptionHolder.builder()
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return handleExceptionInternal(e, holder, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}
