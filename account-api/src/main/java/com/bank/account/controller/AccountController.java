package com.bank.account.controller;

import com.bank.account.exception.AccountException;
import com.bank.account.model.RegisterAccountRequest;
import com.bank.account.service.AccountService;
import com.bank.account.utils.AccountValidator;
import com.bank.account.utils.MessageProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@RestController
@Validated
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<Long> registerAccount(@RequestBody RegisterAccountRequest registerAccountRequest) {
        if (AccountValidator.isRegisterRequestValid(registerAccountRequest)) {
            Long newAccountID = accountService.registerAccount(registerAccountRequest);
            return ResponseEntity.ok(newAccountID);
        }
        throw new AccountException(MessageProvider.getString("account.data.invalid"));
    }

    @GetMapping(path = "/account/{id}/balance")
    public ResponseEntity<BigDecimal> getBalanceByAccountID(@PathVariable("id") @NotNull Long accountID) {
        BigDecimal balance = accountService.getAccountBalanceByAccountID(accountID);
        return ResponseEntity.ok(balance);
    }

    @PutMapping(path = "/account/{id}/balance")
    public ResponseEntity<Void> updateAccountBalance(@PathVariable("id") @NotNull Long accountID,
                                                     @RequestParam("amount") @NotNull BigDecimal amount) {
        accountService.updateAccountBalanceWithAmount(accountID, amount);
        return ResponseEntity.ok().build();
    }
}
