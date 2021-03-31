package io.statistics.transactions.controller;

import io.statistics.transactions.exception.TimeExtendedException;
import io.statistics.transactions.models.Transaction;
import io.statistics.transactions.models.TransactionSummary;
import io.statistics.transactions.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/transactions")
    public ResponseEntity<Void> addTransaction(@RequestBody Transaction transaction) {
        Long start = System.currentTimeMillis();
        try {
            transactionService.addTransaction(transaction);
            Long end = System.currentTimeMillis();
            log.info("Time taken : " + (end - start) + "ms");
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (TimeExtendedException timeExtendedException) {
            Long end = System.currentTimeMillis();
            log.info("Time taken : " + (end - start) + "ms");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/statistics")
    public ResponseEntity<TransactionSummary> getTransactionSummary() {
        Long start = System.currentTimeMillis();
        TransactionSummary transactionSummary = transactionService.getTransactionSummary();
        Long end = System.currentTimeMillis();
        log.info("Time taken : " + (end - start) + "ms");
        return new ResponseEntity<>(transactionSummary, HttpStatus.OK);
    }
}
