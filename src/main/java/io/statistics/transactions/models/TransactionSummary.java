package io.statistics.transactions.models;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionSummary {
    private Double sum;
    private Double avg;
    private Double max;
    private Double min;
    private Long count;
}
