package com.bank.account.exception;

import com.bank.account.utils.MessageProvider;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class DebitException extends OperationException {

    private final BigDecimal currentBalance;

    public DebitException(String message, BigDecimal currentBalance) {
        super(message);
        this.currentBalance = currentBalance;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " " + currentBalanceInfo();
    }

    private String currentBalanceInfo() {
        return MessageProvider.formatString("account.balance.current",
                NumberFormat.getCurrencyInstance().format(currentBalance));
    }
}
