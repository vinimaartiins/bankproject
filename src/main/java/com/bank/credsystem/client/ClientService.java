package com.bank.credsystem.client;

import com.bank.credsystem.exceptions.BadRequestException;
import com.bank.credsystem.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    public ResponseEntity<List<ClientDTO>> findAllClients() {
        return new ResponseEntity<>(this.clientRepository.findAll()
                .stream()
                .map(ClientDTO::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    public ResponseEntity<ClientDTO> findById(Long id) {
        return this.clientRepository
                .findById(id)
                .map(client -> new ResponseEntity<>(new ClientDTO(client), HttpStatus.OK))
                .orElseThrow(() -> new ResourceNotFoundException("Client with " + id + " not found."));
    }

    public ResponseEntity<ClientDTO> insert(ClientDTO client) {
        var c = this.clientRepository.findByCpf(client.getCpf());

        if (c.isPresent())
            throw new BadRequestException("Client with CPF " + client.getCpf() + " already exists.");

        return new ResponseEntity<>(new ClientDTO(this.clientRepository.save(new Client(client))), HttpStatus.CREATED);
    }

    public ResponseEntity<ClientDTO> update(ClientDTO client) {
        var c = this.clientRepository.findById(client.getId());

        if (c.isEmpty()) throw new ResourceNotFoundException("Client not found");

        return new ResponseEntity<>(new ClientDTO(this.clientRepository.save(new Client(client))), HttpStatus.CREATED);
    }

    public ResponseEntity<ClientDTO> deleteById(Long id) {
        this.clientRepository.deleteById(this.findById(id).getBody().getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
