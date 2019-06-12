package com.transactions.util;

import java.math.BigDecimal;

public class Statistics {
    public Statistics(BigDecimal sum, BigDecimal max, BigDecimal min, long count) {
        this.sum = sum;
        this.max = max;
        this.min = min;
        this.count = count;
    }

    public BigDecimal sum;
    public BigDecimal max;
    public BigDecimal min;
    public long count;
}
