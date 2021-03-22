package io.statistics.transactions.service;

import io.statistics.transactions.models.Transaction;
import io.statistics.transactions.models.TransactionSummary;


public interface TransactionService {
    void addTransaction(Transaction transaction);

    TransactionSummary getTransactionSummary();
}
