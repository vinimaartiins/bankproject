package com.bank.credsystem.client;

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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ClientControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClientService clientService;

    private ClientDTO clientDTO;

    @BeforeEach
    void init() {
        this.clientDTO = ClientDTO.builder()
                .cpf("00000000000")
                .name("Client Number One")
                .contactNumber("00000000000")
                .address("Any Address")
                .salary(6000D)
                .build();
    }

    @Test
    void whenFindAllUser_thenReturn200() throws Exception {
        this.mockMvc.perform(get("/clients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void whenInsertValidUser_thenReturns201() throws Exception {
        this.mockMvc.perform(post("/clients")
                        .content(objectMapper.writeValueAsString(this.clientDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    void whenInsertNonValidUserInput_cpf_thenReturn400() throws Exception {
        this.clientDTO.setCpf(null);
        this.mockMvc.perform(post("/clients")
                        .content(objectMapper.writeValueAsString(this.clientDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void whenFindAll_thenReturn200() throws Exception {
        this.clientService.insert(this.clientDTO);
        this.mockMvc.perform(get("/clients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)));
    }

    @Test
    void whenFindById_thenReturn200() throws Exception {
        this.clientService.insert(this.clientDTO);

        this.mockMvc.perform(get("/clients/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cpf").value("00000000000"))
                .andExpect(jsonPath("$.name").value("Client Number One"))
                .andExpect(jsonPath("$.contactNumber").value("00000000000"))
                .andExpect(jsonPath("$.address").value("Any Address"))
                .andExpect(jsonPath("$.salary").value(6000D));
    }

    @Test
    void whenInsertRepeatedInput_cpf_thenReturn400() throws Exception {
        this.clientService.insert(this.clientDTO);

        var repeatedClient = ClientDTO.builder()
                .cpf(this.clientDTO.getCpf())
                .name("Any name 2")
                .contactNumber("19191919191")
                .address("Any address 2")
                .salary(5000D)
                .build();

        this.mockMvc.perform(post("/clients")
                        .content(objectMapper.writeValueAsString(repeatedClient))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenUpdateUser_thenReturn200() throws Exception {
        this.clientService.insert(this.clientDTO);

        this.clientDTO.setId(1L);
        this.clientDTO.setName("New Name");
        this.clientDTO.setAddress("New Address");
        this.clientDTO.setSalary(4000D);

        this.mockMvc.perform(put("/clients")
                        .content(objectMapper.writeValueAsString((this.clientDTO)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.address").value("New Address"))
                .andExpect(jsonPath("$.salary").value(4000D));
    }

    @Test
    void whenDelete_thenReturn200() throws Exception {
        this.clientService.insert(this.clientDTO);

        // Delete client
        this.mockMvc.perform(delete("/clients/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // Verify if there is no client in Database
        this.mockMvc.perform(get("/clients")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(0)));
    }

    @Test
    void whenDeleteNonValidInput_id_thenReturn404() throws Exception {
        this.clientService.insert(this.clientDTO);

        this.mockMvc.perform(delete("/clients/{id}", 2L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}