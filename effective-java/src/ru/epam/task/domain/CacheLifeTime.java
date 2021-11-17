package ru.epam.task.domain;

import java.util.concurrent.TimeUnit;

public final class CacheLifeTime {
    private final TimeUnit timeUnit;
    private final int quantity;

    public CacheLifeTime(TimeUnit unit, int quantity) {
        this.timeUnit = unit;
        this.quantity = quantity;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public int getQuantity() {
        return quantity;
    }

}
