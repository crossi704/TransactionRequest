package com.transactions.service;

import com.transactions.model.Transaction;
import com.transactions.model.TransactionStatistics;
import com.transactions.util.Statistics;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;

import static java.text.MessageFormat.format;

@Service
public class StatisticsService {

    private static final String PATTERN = "{0,number,0.00}";
    private static final TransactionStatistics CLEARED_STATISTIC = TransactionStatistics.builder()
            .sum("0.00")
            .avg("0.00")
            .max("0.00")
            .min("0.00")
            .build();

    private final Clock clock;
    private final DataListInterface dataList;
    private TransactionStatistics statistics;

    public StatisticsService(Clock clock) {
        this.clock = clock;
        this.dataList = new DataList();
        this.statistics = new TransactionStatistics();
    }

    public TransactionStatistics getStatistics() {
        Statistics[] data = dataList.getData();
        BigDecimal sum = null;
        BigDecimal min = null;
        BigDecimal max = null;
        long count = 0;
        for (int i = 0; i < data.length; i++) {
            if (data[i].count > 0) {
                sum = sum == null ? data[i].sum : sum.add(data[i].sum);
                min = min == null ? data[i].min : min.min(data[i].min);
                max = max == null ? data[i].max : max.max(data[i].max);
                count += data[i].count;
            }
        }

        if (count == 0) {
            statistics = CLEARED_STATISTIC;
        } else {
            statistics = TransactionStatistics.builder()
                    .sum(format(PATTERN, sum.setScale(2, BigDecimal.ROUND_HALF_UP)))
                    .avg(format(PATTERN, (sum.setScale(2, BigDecimal.ROUND_UP)).divide(BigDecimal.valueOf(count), RoundingMode.HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP)))
                    .min(format(PATTERN, min.setScale(2, BigDecimal.ROUND_HALF_UP)))
                    .max(format(PATTERN, max.setScale(2, BigDecimal.ROUND_HALF_UP)))
                    .count(count)
                    .build();
        }

        return statistics;
    }

    public void add(Transaction transaction) {
        dataList.add(transaction);
    }

    @Scheduled(cron = "* * * * * *")
    public void updateEverySecond() {
        dataList.purgeOldData(clock.instant());
    }

    public void clear() {
        dataList.clear();
        statistics = CLEARED_STATISTIC;
    }
}