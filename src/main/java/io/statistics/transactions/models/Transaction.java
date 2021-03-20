package io.statistics.transactions.models;

import lombok.*;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Transaction {
    @NotNull
    private Double amount;
    @NotNull
    private Long timestamp;
}
