package com.bank.credsystem.transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> findAll() {
        return this.transactionService.findAll();
    }

    @PostMapping
    public ResponseEntity<TransactionDTO> registerTransaction(@RequestBody @Valid TransactionParams transactionParams) {
        return this.transactionService.registerTransaction(transactionParams.getCardId(), transactionParams.getCardPassword(), transactionParams.getTransaction());
    }
}
