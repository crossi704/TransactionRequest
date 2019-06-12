package com.transactions.model;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Transaction {

    @NotNull
    private BigDecimal amount;

    @NotNull
    private Instant timestamp;

}

