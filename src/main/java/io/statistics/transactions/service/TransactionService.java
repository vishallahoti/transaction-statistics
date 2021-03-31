package io.statistics.transactions.service;

import io.statistics.transactions.exception.TimeExtendedException;
import io.statistics.transactions.models.Transaction;
import io.statistics.transactions.models.TransactionSummary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;

@Service
public class TransactionService {
    private static final List<Transaction> transactionList = new ArrayList<>();
    private static final int TOTAL_SECONDS = 60;
    private Object lock = new Object();
    private TransactionSummary transactionSummary;

    public void addTransaction(Transaction transaction) {
        ZonedDateTime transactionTime = getTransactionTime(transaction.getTimestamp());
        ZonedDateTime currentTime = getCurrentTime();
        if (transactionTime.isBefore(currentTime.minusSeconds(TOTAL_SECONDS))) {
            throw new TimeExtendedException("Transaction is older than 60 seconds");
        }
        synchronized (lock) {
            transactionList.add(transaction);
            calculateStatistics();
        }
    }

    public TransactionSummary getTransactionSummary() {
        return transactionSummary;
    }

    @Async
    public void calculateStatistics() {
        DoubleSummaryStatistics doubleSummaryStatistics = transactionList.stream().mapToDouble(Transaction::getAmount)
                .summaryStatistics();
        transactionSummary = new TransactionSummary(doubleSummaryStatistics.getSum(), doubleSummaryStatistics.getAverage(), doubleSummaryStatistics.getMax(), doubleSummaryStatistics.getMin(), doubleSummaryStatistics.getCount());
    }

    @Scheduled(fixedRate = 60000, initialDelay = 5000)
    public void clearOldData() {
        synchronized (lock) {
            transactionList.removeIf(
                    transaction -> getTransactionTime(transaction.getTimestamp()).isBefore(getCurrentTime().minusSeconds(TOTAL_SECONDS)));
            calculateStatistics();
        }
    }

    private ZonedDateTime getTransactionTime(Long transactionTime) {
        return Instant.ofEpochMilli(transactionTime).atZone(ZoneOffset.UTC);
    }

    private ZonedDateTime getCurrentTime() {
        return Instant.now().atZone(ZoneOffset.UTC);
    }
}
