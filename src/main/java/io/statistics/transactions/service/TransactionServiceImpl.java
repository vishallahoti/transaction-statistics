package io.statistics.transactions.service;

import io.statistics.transactions.exception.TimeExtendedException;
import io.statistics.transactions.models.Transaction;
import io.statistics.transactions.models.TransactionSummary;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.DoubleSummaryStatistics;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;


@Service
public class TransactionServiceImpl implements TransactionService {

    private final Queue<Transaction> transactions;
    private final int TOTAL_SECONDS = 60;

    public TransactionServiceImpl() {
        super();
        this.transactions = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void addTransaction(Transaction transaction) {
        ZonedDateTime transactionTime = getTransactionTime(transaction.getTimestamp());
        ZonedDateTime currentTime = getCurrentTime();
        if (transactionTime.isBefore(currentTime.minusSeconds(TOTAL_SECONDS))) {
            throw new TimeExtendedException("Transaction is older than 60 seconds");
        }
        transactions.add(transaction);
    }

    @Override
    public TransactionSummary getTransactionSummary() {
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
}
