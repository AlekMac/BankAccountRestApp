package com.bank.account.service;

import com.bank.account.exception.AccountException;
import com.bank.account.exception.ConcurrentUpdateException;
import com.bank.account.exception.DebitException;
import com.bank.account.model.RegisterAccountRequest;
import com.bank.account.repository.AccountRepository;
import com.bank.account.repository.entity.Account;
import com.bank.account.utils.MessageProvider;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private final OperationService operationService;

    public AccountService(AccountRepository accountRepository, OperationService operationService) {
        this.accountRepository = accountRepository;
        this.operationService = operationService;
    }

    @Transactional
    public Long registerAccount(RegisterAccountRequest registerAccountRequest) {
        Account account = new Account();
        account.setFirstName(registerAccountRequest.getFirstName());
        account.setLastName(registerAccountRequest.getLastName());
        return accountRepository.save(account).getId();
    }

    public BigDecimal getAccountBalanceByAccountID(Long accountID) {
        return getAccountByID(accountID).getBalance();
    }

    @Transactional
    public void updateAccountBalanceWithAmount(Long accountID, BigDecimal amount) {
        try {
            Account account = getAccountByID(accountID);
            BigDecimal newBalance = account.getBalance().add(amount);
            checkDebit(account.getBalance(), newBalance);
            account.setBalance(newBalance);
            accountRepository.update(account);
            operationService.createOperation(accountID, amount);
        } catch (OptimisticLockingFailureException e) {
            throw new ConcurrentUpdateException(MessageProvider.getString("account.balance.concurrent.update"));
        }
    }

    private void checkDebit(BigDecimal accountBalance, BigDecimal newBalance) {
        if (isDebit(newBalance)) {
            throw new DebitException(MessageProvider.getString("account.balance.update.fail"), accountBalance);
        }
    }

    private boolean isDebit(BigDecimal balance) {
        return balance.signum() < 0;
    }

    private Account getAccountByID(Long accountID) {
        return accountRepository
                .findById(accountID)
                .orElseThrow(() -> new AccountException(MessageProvider.getString("account.not.found")));
    }
}
