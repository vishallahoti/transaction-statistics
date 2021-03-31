package io.statistics.transactions.service;

import io.statistics.transactions.exception.TimeExtendedException;
import io.statistics.transactions.models.Transaction;
import io.statistics.transactions.models.TransactionSummary;
import io.statistics.transactions.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    private final int TOTAL_SECONDS = 60;
    private Object lock = new Object();

    public void addTransaction(Transaction transaction) {
        ZonedDateTime transactionTime = getTransactionTime(transaction.getTimestamp());
        ZonedDateTime currentTime = getCurrentTime();
        if (transactionTime.isBefore(currentTime.minusSeconds(TOTAL_SECONDS))) {
            throw new TimeExtendedException("Transaction is older than 60 seconds");
        }
        transactionRepository.save(transaction);
    }

    public TransactionSummary getTransactionSummary() {
        List<Transaction> transactions = new ArrayList<>();
        transactionRepository.findAll().forEach(transactions::add);
        if (!transactions.isEmpty()) {
            DoubleSummaryStatistics summary = transactions.stream()
                    .filter(t -> getTransactionTime(t.getTimestamp()).isAfter(getCurrentTime().minusSeconds(TOTAL_SECONDS)))
                    .collect(Collectors.summarizingDouble(Transaction::getAmount));
            return new TransactionSummary(summary.getSum(), summary.getAverage(), summary.getMax(),
                    summary.getMin(), summary.getCount());
        } else {
            return new TransactionSummary(0.0, 0.0, 0.0, 0.0, 0L);
        }
    }

    private ZonedDateTime getTransactionTime(Long transactionTime) {
        return Instant.ofEpochMilli(transactionTime).atZone(ZoneOffset.UTC);
    }

    private ZonedDateTime getCurrentTime() {
        return Instant.now().atZone(ZoneOffset.UTC);
    }

    @Scheduled(fixedRate = 60000, initialDelay = 5000)
    public void clearOldData() {
        synchronized (lock) {
            transactionRepository.findAll().removeIf(
                    transaction -> getTransactionTime(transaction.getTimestamp()).isBefore(getCurrentTime().minusSeconds(TOTAL_SECONDS)));
        }
    }
}
