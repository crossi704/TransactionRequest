package com.transactions.service;

import com.transactions.model.Transaction;
import com.transactions.util.Statistics;

import java.time.Instant;

interface DataListInterface {
    public void add(Transaction newValue);
    public void clear();
    public Statistics[] getData();
    public void purgeOldData(Instant now);
}
