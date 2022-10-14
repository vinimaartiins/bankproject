package com.bank.credsystem.card;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Log4j2
@RestController
@RequestMapping(value = "/cards")
public class CardController {
    @Autowired
    private CardService cardService;

    @GetMapping
    public ResponseEntity<List<CardDTO>> findAll() {
        log.info("------> findAllCards Service");
        return cardService.findAll();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CardDTO> findById(@PathVariable Long id) {
        log.info("------> findById Card Service");
        return cardService.findById(id);
    }


    @PostMapping
    public ResponseEntity<CardDTO> generate(@RequestBody @Valid CardCreatorParams cardParams) {
        log.info("------> generate card Service");
        return this.cardService.generate(cardParams);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CardDTO> delete(@PathVariable Long id) {
        log.info("------> delete card Service");
        return this.cardService.deleteById(id);
    }

    @PutMapping
    public ResponseEntity<CardDTO> enableCard(@RequestBody @Valid CardActiveParams cardActiveParams) {
        log.info("------> enableCard Service");
        return this.cardService.enableCard(cardActiveParams);
    }
}
