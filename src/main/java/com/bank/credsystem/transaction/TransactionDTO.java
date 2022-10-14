package com.bank.credsystem.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDTO {
    private long id;
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0.")
    private double amount;
    private LocalDateTime dateTime;

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.amount = transaction.getAmount();
        this.dateTime = transaction.getDateTime();
    }
}
