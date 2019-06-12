package com.transactions.service;

import com.transactions.model.Transaction;
import com.transactions.util.Statistics;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class DataListTest {
    private DataListInterface dataListInterface = new DataList();
    BigDecimal amount1 = new BigDecimal("1");
    BigDecimal amount2 = new BigDecimal("2");
    BigDecimal amount3 = new BigDecimal("3");
    Transaction t1 = new Transaction(amount1, Instant.now());
    Transaction t2 = new Transaction(amount2, Instant.now());
    Transaction t3 = new Transaction(amount3, Instant.now());

    @Test
    public void add() {
        dataListInterface.add(t1);
        dataListInterface.add(t2);
        dataListInterface.add(t3);
        Statistics[] data = dataListInterface.getData();
        assertNotEquals(data.length, 0);
    }

    @Test
    public void clear() {
        dataListInterface = new DataList();
        dataListInterface.add(t1);
        dataListInterface.add(t2);
        dataListInterface.add(t3);
        dataListInterface.clear();
        Statistics[] data = dataListInterface.getData();
        assertNull(data[0].min);
        assertNull(data[0].max);
        assertEquals(data[0].sum, BigDecimal.ZERO);
        assertEquals(data[0].count, 0);
        assertNull(data[1].min);
        assertNull(data[1].max);
        assertEquals(data[1].sum, BigDecimal.ZERO);
        assertEquals(data[1].count, 0);
        assertNull(data[2].min);
        assertNull(data[2].max);
        assertEquals(data[2].sum, BigDecimal.ZERO);
        assertEquals(data[2].count, 0);
    }

    @Test
    public void purgeOldData() {
        // ToDo
    }

    @Test
    public void getData() {
        // ToDo
    }
}