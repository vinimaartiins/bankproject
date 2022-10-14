package com.bank.credsystem.transaction;

import com.bank.credsystem.card.Card;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "tb_transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double amount;
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "card_transaction_id")
    private Card card;

    public Transaction(TransactionDTO transactionDTO, Card card) {
        this.amount = transactionDTO.getAmount();
        this.dateTime = LocalDateTime.now();
        this.card = card;
    }
}
