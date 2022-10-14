package com.bank.credsystem.card;

import com.bank.credsystem.client.ClientRepository;
import com.bank.credsystem.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardService {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    public ResponseEntity<List<CardDTO>> findAll() {
        return new ResponseEntity<>(cardRepository.findAll()
                .stream()
                .map(CardDTO::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    public ResponseEntity<CardDTO> generate(CardCreatorParams cardParams) {
        var client = this.clientRepository.findById(cardParams.getClientId());

        if (client.isEmpty())
            throw new ResourceNotFoundException("Client id " + cardParams.getClientId() + " not found.");

        return new ResponseEntity<>(
                new CardDTO(this.cardRepository.save(
                        CardFactory.makeCard(client.get(), cardParams)
                )), HttpStatus.CREATED);
    }

    public ResponseEntity<CardDTO> deleteById(Long id) {
        this.cardRepository.deleteById(this.findById(id).getBody().getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<CardDTO> findById(Long id) {
        return this.cardRepository
                .findById(id)
                .map(card -> new ResponseEntity<>(new CardDTO(card), HttpStatus.OK))
                .orElseThrow(() -> new ResourceNotFoundException("Card with id " + id + " does not exist."));
    }

    public ResponseEntity<CardDTO> enableCard(CardActiveParams cardActiveParams) {
        var card = this.cardRepository.findById(cardActiveParams.getCardId());
        if (card.isEmpty())
            throw new ResourceNotFoundException("Card with id " + cardActiveParams.getCardId() + " does not exist.");
        card.get().setActive(cardActiveParams.isEnableCard());
        return new ResponseEntity<>(new CardDTO(this.cardRepository.save(card.get())), HttpStatus.OK);
    }
}
