package com.bank.credsystem.transaction;

import com.bank.credsystem.card.Card;
import com.bank.credsystem.card.CardRepository;
import com.bank.credsystem.exceptions.ResourceNotFoundException;
import com.bank.credsystem.exceptions.UnauthorizedException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CardRepository cardRepository;

    public ResponseEntity<List<TransactionDTO>> findAll() {
        log.info("------> find All Cards Service");
        return new ResponseEntity<>(this.transactionRepository.findAll().stream().map(TransactionDTO::new).collect(Collectors.toList()), HttpStatus.OK);
    }

    public ResponseEntity<TransactionDTO> registerTransaction(Long cardId, String cardPassword, TransactionDTO transaction) {
        log.info("------> register Transaction Service");

        var card = this.cardRepository.findById(cardId);

        this.validCard(card);
        this.verifyPassword(card, cardPassword);
        this.verifyCardStatus(card);

        var cardLimit = card.get().getCardLimit();
        var transactionAmount = transaction.getAmount();

        this.verifyCardBalance(cardLimit, transactionAmount);

        card.get().setCardLimit(cardLimit - transactionAmount);

        return new ResponseEntity<>(new TransactionDTO(this.transactionRepository.save(new Transaction(transaction, card.get()))), HttpStatus.CREATED);
    }

    private void validCard(Optional<Card> card) {
        if (card.isEmpty()) throw new ResourceNotFoundException("Card not found.");
    }

    private void verifyPassword(Optional<Card> card, String cardPassword) {
        if (!card.get().getPassword().equals(cardPassword)) throw new UnauthorizedException("Card password is wrong.");
    }

    private void verifyCardStatus(Optional<Card> card) {
        if (!card.get().isActive()) throw new UnauthorizedException("Card is currently disabled.");
    }

    private void verifyCardBalance(Double balance, Double transactionAmount) {
        if (balance < transactionAmount) throw new UnauthorizedException("Insufficient founds on the card.");
    }
}
