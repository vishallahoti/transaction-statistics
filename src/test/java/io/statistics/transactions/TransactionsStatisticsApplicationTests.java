package io.statistics.transactions;

import com.google.gson.Gson;
import io.statistics.transactions.controller.TransactionController;
import io.statistics.transactions.models.Transaction;
import io.statistics.transactions.service.TransactionService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TransactionsStatisticsApplicationTests {

    @Autowired
    private TransactionController transactionController;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private Gson gson;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void contextLoads() {
        Assertions.assertNotNull(transactionController);
    }


    private List<Transaction> addSuccessfulTransactions() {
        List<Transaction> successfulTransactions = new ArrayList<>();
        Instant time = Instant.now();
        successfulTransactions.add(new Transaction("1", 10.0, time.toEpochMilli()));
        successfulTransactions.add(new Transaction("2", 20.0, time.toEpochMilli()));
        successfulTransactions.add(new Transaction("3", 30.0, time.toEpochMilli()));
        successfulTransactions.add(new Transaction("4", 40.0, time.toEpochMilli()));
        successfulTransactions.add(new Transaction("5", 50.0, time.toEpochMilli()));
        return successfulTransactions;
    }

    @Test
    @Order(2)
    public void validTransactionsTest() throws Exception {

        for (Transaction transaction : addSuccessfulTransactions()) {
            String jsonTransaction = gson.toJson(transaction);
            mockMvc.perform(post("/transactions")
                    .content(jsonTransaction)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());
        }
    }

    private List<Transaction> errorTransactions() {
        List<Transaction> errorTransaction = new ArrayList<>();
        Instant time = Instant.now();
        errorTransaction.add(new Transaction("1", 10.0, time.minusSeconds(65).toEpochMilli()));
        return errorTransaction;
    }

    @Test
    @Order(3)
    public void invalidTimestampTest() throws Exception {

        for (Transaction transaction : errorTransactions()) {
            String jsonTransactions = gson.toJson(transaction);
            mockMvc.perform(post("/transactions")
                    .content(jsonTransactions)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }
    }

    @Test
    @Order(4)
    public void getTransactionSummaryResult() throws Exception {
        mockMvc.perform(get("/statistics").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("count", is(5)))
                .andExpect(jsonPath("sum", is(150.0)))
                .andExpect(jsonPath("avg", is(30.0)))
                .andExpect(jsonPath("max", is(50.0)))
                .andExpect(jsonPath("min", is(10.0)));
    }

}