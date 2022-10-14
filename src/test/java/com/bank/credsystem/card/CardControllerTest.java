package com.bank.credsystem.card;

import com.bank.credsystem.client.ClientDTO;
import com.bank.credsystem.client.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CardControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CardService cardService;

    @Autowired
    private ClientService clientService;

    @BeforeEach
    void init() {
        ClientDTO clientDTO = ClientDTO.builder()
                .cpf("00000000000")
                .name("Client Number One")
                .contactNumber("00000000000")
                .address("Any Address")
                .salary(6000D)
                .build();

        this.clientService.insert(clientDTO);
    }

    @Test
    void whenCreateCard_thenReturn201() throws Exception {
        var cardParams = CardCreatorParams.builder()
                .clientId(1L)
                .cardPassword("123456")
                .flag(CardFlag.AMERICAN_EXPRESS)
                .build();

        this.mockMvc.perform(post("/cards")
                        .content(objectMapper.writeValueAsString(cardParams))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                // Verify if card limit respect the conditions.
                // Must be at least 300 and maximum 2000
                // Where 1150 represents the average between it
                .andExpect(jsonPath("$.cardLimit", closeTo(1150, 850)))
                .andExpect(jsonPath("$.active").value(false));
    }

    @Test
    void whenFindAllCards_thenReturn200() throws Exception {
        var cardParams = CardCreatorParams.builder()
                .clientId(1L)
                .cardPassword("123456")
                .flag(CardFlag.AMERICAN_EXPRESS)
                .build();
        this.cardService.generate(cardParams);

        this.mockMvc.perform(get("/cards")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)));
    }

    @Test
    void whenFindCardById_thenReturn200() throws Exception {
        var cardParams = CardCreatorParams.builder()
                .clientId(1L)
                .cardPassword("123456")
                .flag(CardFlag.AMERICAN_EXPRESS)
                .build();
        this.cardService.generate(cardParams);

        this.mockMvc.perform(get("/cards/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cardHolderName").value("CLIENT N ONE"))
                .andExpect(jsonPath("$.cardLimit", closeTo(1150, 850)))
                .andExpect(jsonPath("$.cardFlag").value("AMERICAN_EXPRESS"))
                .andExpect(jsonPath("$.active").value(false));
    }

    @Test
    void whenCreateCard_wrongClient_thenReturn404() throws Exception {
        var cardParams = CardCreatorParams.builder()
                .clientId(2L) // Non-existing client id
                .cardPassword("123456")
                .flag(CardFlag.AMERICAN_EXPRESS)
                .build();

        this.mockMvc.perform(post("/cards")
                        .content(objectMapper.writeValueAsString(cardParams))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void whenEnableCard_thenReturn200() throws Exception {
        var cardParams = CardCreatorParams.builder()
                .clientId(1L)
                .cardPassword("123456")
                .flag(CardFlag.AMERICAN_EXPRESS)
                .build();
        this.cardService.generate(cardParams);


        var cardActiveParams = CardActiveParams.builder()
                .cardId(1L)
                .enableCard(true)
                .build();

        this.mockMvc.perform(put("/cards")
                        .content(objectMapper.writeValueAsString(cardActiveParams))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void whenEnableNonValidInput_cardId_thenReturn404() throws Exception {
        var cardParams = CardCreatorParams.builder()
                .clientId(1L)
                .cardPassword("123456")
                .flag(CardFlag.AMERICAN_EXPRESS)
                .build();
        this.cardService.generate(cardParams);

        var cardActiveParams = CardActiveParams.builder()
                .cardId(2L)
                .enableCard(true)
                .build();

        this.mockMvc.perform(put("/cards")
                        .content(objectMapper.writeValueAsString(cardActiveParams))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void whenDeleteById_thenReturn200() throws Exception {
        var cardParams = CardCreatorParams.builder()
                .clientId(1L)
                .cardPassword("123456")
                .flag(CardFlag.AMERICAN_EXPRESS)
                .build();
        this.cardService.generate(cardParams);

        this.mockMvc.perform(delete("/cards/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void whenDeleteNonValidInput_id_thenReturn404() throws Exception {
        var cardParams = CardCreatorParams.builder()
                .clientId(1L)
                .cardPassword("123456")
                .flag(CardFlag.AMERICAN_EXPRESS)
                .build();
        this.cardService.generate(cardParams);

        this.mockMvc.perform(delete("/cards/{id}", 2L) // non valid id
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
