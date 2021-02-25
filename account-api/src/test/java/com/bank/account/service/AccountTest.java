package com.bank.account.service;


import com.bank.account.exception.ConcurrentUpdateException;
import com.bank.account.exception.DebitException;
import com.bank.account.model.RegisterAccountRequest;
import com.bank.account.repository.AccountRepository;
import com.bank.account.repository.entity.Account;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.OptimisticLockingFailureException;

import java.math.BigDecimal;
import java.util.Optional;

public class AccountTest {

    @InjectMocks
    AccountService accountService;

    @Mock
    AccountRepository accountRepository;

    @Mock
    OperationService operationService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldReturnNewAccountIdAfterRegistration() {
        RegisterAccountRequest request = new RegisterAccountRequest("foo", "bar");

        Account requested = new Account();
        requested.setFirstName(request.getFirstName());
        requested.setLastName(request.getLastName());

        Account registered = new Account();
        registered.setId(1L);
        registered.setFirstName(request.getFirstName());
        registered.setLastName(request.getLastName());

        Mockito.when(accountRepository.save(requested)).thenReturn(registered);
        Long id = accountService.registerAccount(request);

        Mockito.verify(accountRepository, Mockito.times(1)).save(requested);
        Assertions.assertEquals(1L, id);
    }

    @Test
    public void shouldThrowAnExceptionWhenDebitRequested() {
        Account account = new Account();
        account.setId(1L);
        account.setBalance(BigDecimal.ONE);

        Mockito.when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        Assertions.assertThrows(DebitException.class,
                () -> accountService.updateAccountBalanceWithAmount(account.getId(), BigDecimal.TEN.negate()));
    }

    @Test
    public void shouldUpdateAccountBalanceWithAmount() {
        Account before = new Account();
        before.setId(1L);
        before.setBalance(BigDecimal.ONE);

        Account after = new Account();
        after.setId(before.getId());
        after.setBalance(BigDecimal.ONE.add(before.getBalance()));
        Mockito.when(accountRepository.findById(before.getId())).thenReturn(Optional.of(before));
        accountService.updateAccountBalanceWithAmount(before.getId(), BigDecimal.ONE);
        Mockito.verify(accountRepository, Mockito.times(1)).update(after);
        Mockito.verify(operationService, Mockito.times(1)).createOperation(after.getId(), BigDecimal.ONE);
    }

    @Test
    public void shouldThrowAnExceptionWhenConcurrentUpdateRequested() {
        Account old = new Account();
        old.setId(1L);
        old.setBalance(BigDecimal.ONE);

        Account updated = new Account();
        updated.setId(old.getId());
        updated.setBalance(old.getBalance().add(BigDecimal.ONE));

        Mockito.when(accountRepository.findById(old.getId())).thenReturn(Optional.of(old));
        Mockito.doThrow(new OptimisticLockingFailureException("")).when(accountRepository).update(updated);

        Assertions.assertThrows(ConcurrentUpdateException.class,
                () -> accountService.updateAccountBalanceWithAmount(old.getId(), BigDecimal.ONE));
    }

}
