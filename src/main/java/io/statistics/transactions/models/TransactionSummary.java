package io.statistics.transactions.models;

import lombok.*;

@Getter
@AllArgsConstructor
public class TransactionSummary {
    private Double sum;
    private Double avg;
    private Double max;
    private Double min;
    private Long count;
}
