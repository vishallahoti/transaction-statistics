package io.statistics.transactions.repository;

import io.statistics.transactions.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
}
