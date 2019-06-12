package com.transactions.service;

import com.transactions.model.Transaction;
import com.transactions.util.Statistics;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;

public class DataList implements DataListInterface {

    private static final Statistics CLEARED_STATISTIC = new Statistics(BigDecimal.ZERO, null, null, 0);

    private final Statistics[] data;
    private final Object[] dataLock;

    public DataList() {
        data = new Statistics[61];
        dataLock = new Object[61];
        for (int i = 0; i < 61; i++) {
            data[i] = CLEARED_STATISTIC;
            dataLock[i] = new Object();
        }
    }

    public void add(Transaction newValue) {
        int index = getHash(newValue.getTimestamp());
        BigDecimal amount = newValue.getAmount();
        synchronized (dataLock[index]) {
            Statistics current = data[index];
            if (current.count == 0) {
                data[index] = new Statistics(amount, amount, amount, 1);
            } else {
                data[index] = new Statistics(
                        current.sum.add(amount),
                        max(current.max, amount),
                        min(current.min, amount),
                        current.count + 1);
            }
        }
    }

    public void clear() {
        for (int i = 0; i < data.length; i++) {
            synchronized (dataLock[i]) {
                data[i] = CLEARED_STATISTIC;
            }
        }
    }

    public void purgeOldData(Instant now) {
        int indexToRemove = (getHash(now) + 1) % 61;
        synchronized (dataLock[indexToRemove]) {
            data[indexToRemove] = CLEARED_STATISTIC;
        }
    }

    private int getHash(Instant instant) {
        return (int) (instant.toEpochMilli() / 1000 % 61);
    }

    public Statistics[] getData() {
        return Arrays.copyOf(data, data.length);
    }

    private BigDecimal max(BigDecimal currentValue, BigDecimal newValue) {
        if (currentValue == null) {
            return newValue;
        }
        return currentValue.compareTo(newValue) > 0 ? currentValue : newValue;
    }

    private BigDecimal min(BigDecimal currentValue, BigDecimal newValue) {
        if (currentValue == null) {
            return newValue;
        }
        return currentValue.compareTo(newValue) < 0 ? currentValue : newValue;
    }

}