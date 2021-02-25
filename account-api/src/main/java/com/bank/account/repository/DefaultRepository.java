package com.bank.account.repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public abstract class DefaultRepository<T> {

    private final AtomicLong idCounter = new AtomicLong(0);

    private final Map<Long, T> table = new ConcurrentHashMap<>();

    abstract T save(T entity);

    abstract T copy(T entity);

    public Optional<T> findById(Long id) {
        T entity = table.get(id);
        return Optional.ofNullable(entity != null ? copy(entity) : null);
    }

    final Long generateID() {
        return idCounter.addAndGet(1);
    }

    final void persist(Long id, T entity) {
        table.put(id, entity);
    }
}
