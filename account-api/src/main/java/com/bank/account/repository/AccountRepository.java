package com.bank.account.repository;

import com.bank.account.exception.AccountException;
import com.bank.account.repository.entity.Account;
import com.bank.account.utils.MessageProvider;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Repository;

@Repository
public class AccountRepository extends DefaultRepository<Account> {

    @Override
    public Account save(Account account) {
        Long id = generateID();
        account.setId(id);
        persist(id, account);
        return account;
    }

    @Override
    Account copy(Account entity) {
        Account account = new Account();
        account.setId(entity.getId());
        account.setFirstName(entity.getFirstName());
        account.setLastName(entity.getLastName());
        account.setBalance(entity.getBalance());
        account.setVersion(entity.getVersion());
        return account;
    }

    public void update(Account account) {
        int currentVersion = findById(account.getId())
                .map(Account::getVersion)
                .orElseThrow(() -> new AccountException(MessageProvider.getString("account.not.found")));
        if (account.getVersion() == currentVersion) {
            account.setVersion(currentVersion + 1);
            persist(account.getId(), account);
        } else {
            throw new OptimisticLockingFailureException("");
        }
    }
}
