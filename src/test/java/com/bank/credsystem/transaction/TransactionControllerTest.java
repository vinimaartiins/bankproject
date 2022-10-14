package com.bank.credsystem.transaction;

import com.bank.credsystem.card.CardActiveParams;
import com.bank.credsystem.card.CardCreatorParams;
import com.bank.credsystem.card.CardFlag;
import com.bank.credsystem.card.CardService;
import com.bank.credsystem.client.ClientDTO;
import com.bank.credsystem.client.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientService clientService;

    @Autowired
    private CardService cardService;

    @BeforeEach
    void init() {
        var clientDTO = ClientDTO.builder()
                .cpf("00000000000")
                .name("Client Number One")
                .contactNumber("00000000000")
                .address("Any Address")
                .salary(6000D)
                .build();

        var cardParams = CardCreatorParams.builder()
                .clientId(1L)
                .cardPassword("123456")
                .flag(CardFlag.AMERICAN_EXPRESS)
                .build();

        var cardActiveParams = CardActiveParams.builder()
                .cardId(1L)
                .enableCard(true)
                .build();

        this.clientService.insert(clientDTO);
        this.cardService.generate(cardParams);
        this.cardService.enableCard(cardActiveParams);
    }

    @Test
    void whenGenerateTransaction_thenReturn201() throws Exception {
        var transaction = TransactionDTO.builder()
                .amount(300D)
                .build();
        var transactionParams = TransactionParams.builder()
                .cardId(1L)
                .transaction(transaction)
                .cardPassword("123456")
                .build();

        this.mockMvc.perform(post("/transactions")
                        .content(objectMapper.writeValueAsString(transactionParams))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void whenGenerateTransactionConsumeCardLimit_thenReturn200() throws Exception {
        var transaction = TransactionDTO.builder()
                .amount(300D)
                .build();
        var transactionParams = TransactionParams.builder()
                .cardId(1L)
                .transaction(transaction)
                .cardPassword("123456")
                .build();

        var cardBefore = this.cardService.findById(1L).getBody();
        Assertions.assertNotNull(cardBefore);

        this.mockMvc.perform(post("/transactions")
                        .content(objectMapper.writeValueAsString(transactionParams))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        var cardAfter = this.cardService.findById(1L).getBody();
        Assertions.assertNotNull(cardAfter);
        Assertions.assertEquals(1800D, cardBefore.getCardLimit()); // Value before transaction
        Assertions.assertEquals(1500D, cardAfter.getCardLimit()); // Value after transaction
    }

    @Test
    void whenGenerateTransactionWithOutLimit_thenReturn401() throws Exception {
        var transaction = TransactionDTO.builder()
                .amount(3000D) // Value above card limit
                .build();
        var transactionParams = TransactionParams.builder()
                .cardId(1L)
                .transaction(transaction)
                .cardPassword("123456")
                .build();

        this.mockMvc.perform(post("/transactions")
                        .content(objectMapper.writeValueAsString(transactionParams))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenGenerateTransactionWithDisabledCard_thenReturn401() throws Exception {
        var transaction = TransactionDTO.builder()
                .amount(3000D) // Value above card limit
                .build();
        var transactionParams = TransactionParams.builder()
                .cardId(1L)
                .transaction(transaction)
                .cardPassword("123456")
                .build();
        this.cardService.enableCard(CardActiveParams.builder().cardId(1L).enableCard(false).build());

        this.mockMvc.perform(post("/transactions")
                        .content(objectMapper.writeValueAsString(transactionParams))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenFindAllTransactions_thenReturn200() throws Exception {
        var transaction = TransactionDTO.builder()
                .amount(300D) // Value above card limit
                .build();
        var transactionParams = TransactionParams.builder()
                .cardId(1L)
                .transaction(transaction)
                .cardPassword("123456")
                .build();

        this.mockMvc.perform(post("/transactions")
                        .content(objectMapper.writeValueAsString(transactionParams))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());

        this.mockMvc.perform(get("/transactions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)));
    }
}