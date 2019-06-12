package com.transactions.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionStatistics {
    String sum;
    String avg;
    String max;
    String min;
    long count;
}
