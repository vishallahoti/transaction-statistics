package io.statistics.transactions.service;

import io.statistics.transactions.models.Transaction;
import io.statistics.transactions.models.TransactionSummary;
import org.springframework.stereotype.Service;


public interface TransactionService {
    void addTransaction(Transaction transaction);

    TransactionSummary getTransactionSummary();
}
