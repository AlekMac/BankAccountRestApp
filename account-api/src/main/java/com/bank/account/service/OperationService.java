package com.bank.account.service;

import com.bank.account.repository.OperationRepository;
import com.bank.account.repository.entity.Operation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class OperationService {

    private final OperationRepository operationRepository;

    public OperationService(OperationRepository operationRepository) {
        this.operationRepository = operationRepository;
    }

    @Transactional
    void createOperation(Long accountID, BigDecimal amount) {
        Operation operation = new Operation();
        operation.setAccountID(Objects.requireNonNull(accountID));
        operation.setAmount(Objects.requireNonNull(amount));
        operation.setDateTime(LocalDateTime.now());
        operationRepository.save(operation);
    }
}
