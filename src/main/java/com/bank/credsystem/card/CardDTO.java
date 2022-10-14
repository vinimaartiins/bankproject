package com.bank.credsystem.card;

import com.bank.credsystem.transaction.TransactionDTO;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CardDTO {
    private final Long id;
    private final String number;
    private final String cardHolderName;
    private final String expirationDate;
    private final double cardLimit;
    private final CardFlag cardFlag;
    private final boolean active;
    private final List<TransactionDTO> transactionList;
    private final String cvv;

    public CardDTO(Card card) {
        this.id = card.getId();
        this.number = card.getNumber();
        this.cardHolderName = card.getCardHolderName();
        this.expirationDate = card.getExpirationDate();
        this.cvv = card.getCvv();
        this.cardLimit = card.getCardLimit();
        this.cardFlag = card.getCardFlag();
        this.active = card.isActive();
        this.transactionList = card.getTransaction() == null ? new ArrayList<>() : card.getTransaction().stream().map(TransactionDTO::new).collect(Collectors.toList());
    }
}
