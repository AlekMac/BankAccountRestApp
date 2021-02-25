package com.bank.account.service;

import com.bank.account.repository.OperationRepository;
import com.bank.account.repository.entity.Operation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

public class OperationTest {

    @InjectMocks
    OperationService operationService;

    @Mock
    OperationRepository operationRepository;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldCreateAnOperationForBalanceUpdate() {
        operationService.createOperation(1L, BigDecimal.ONE);
        operationService.createOperation(1L, BigDecimal.ONE.negate());
        Mockito.verify(operationRepository, Mockito.times(2)).save(Mockito.any(Operation.class));
    }

}
