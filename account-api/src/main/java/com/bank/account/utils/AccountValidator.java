package com.bank.account.utils;

import com.bank.account.model.RegisterAccountRequest;

import java.util.Objects;

public class AccountValidator {

    public static boolean isRegisterRequestValid(RegisterAccountRequest registerAccountRequest) {
        return Objects.nonNull(registerAccountRequest.getFirstName())
                && Objects.nonNull(registerAccountRequest.getLastName());
    }
}
