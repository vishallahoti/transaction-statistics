package io.statistics.transactions.controller;

import io.statistics.transactions.exception.TimeExtendedException;
import io.statistics.transactions.models.Transaction;
import io.statistics.transactions.models.TransactionSummary;
import io.statistics.transactions.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/transactions")
    public ResponseEntity<Void> addTransaction(@RequestBody Transaction transaction) {
        try {
            transactionService.addTransaction(transaction);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (TimeExtendedException timeExtendedException) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/statistics")
    public ResponseEntity<TransactionSummary> getTransactionSummary() {
        TransactionSummary transactionSummary = transactionService.getTransactionSummary();
        return new ResponseEntity<>(transactionSummary, HttpStatus.OK);
    }
}
