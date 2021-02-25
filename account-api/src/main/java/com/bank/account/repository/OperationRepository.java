package com.bank.account.repository;

import com.bank.account.repository.entity.Operation;
import org.springframework.stereotype.Repository;

@Repository
public class OperationRepository extends DefaultRepository<Operation> {

    @Override
    public Operation save(Operation operation){
        Long id = generateID();
        operation.setId(id);
        persist(id, operation);
        return operation;
    }

    @Override
    Operation copy(Operation entity) {
        Operation operation = new Operation();
        operation.setId(entity.getId());
        operation.setDateTime(entity.getDateTime());
        operation.setAmount(entity.getAmount());
        operation.setAccountID(entity.getAccountID());
        return operation;
    }
}
