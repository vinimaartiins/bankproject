package com.bank.credsystem.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    public ResponseEntity<List<ClientDTO>> findAllClients() {
        log.info("------> findAllClients Service");
        return this.clientService.findAllClients();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ClientDTO> findById(@PathVariable Long id) {
        log.info("------> findById Service");
        return this.clientService.findById(id);
    }

    @PostMapping
    public ResponseEntity<ClientDTO> insert(@RequestBody @Valid ClientDTO client) {
        log.info("------> insert Client Service");
        return this.clientService.insert(client);
    }

    @PutMapping
    public ResponseEntity<ClientDTO> update(@RequestBody ClientDTO client) {
        log.info("------> update Client Service");
        return this.clientService.update(client);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ClientDTO> deleteById(@PathVariable Long id) {
        log.info("------> delete Client Service");
        return this.clientService.deleteById(id);
    }
}
