package io.statistics.transactions.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "transactions", indexes = {@Index(name = "index1", columnList = "transactionId,amount,timestamp")})
public class Transaction {
    @Id
    private String transactionId = UUID.randomUUID().toString();
    @NotNull
    private Double amount;
    @NotNull
    private Long timestamp;
}
