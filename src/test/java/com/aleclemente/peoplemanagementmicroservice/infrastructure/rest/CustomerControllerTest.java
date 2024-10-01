package com.aleclemente.peoplemanagementmicroservice.infrastructure.rest;

import com.aleclemente.peoplemanagementmicroservice.application.usecases.CreateCustomerUseCase;
import com.aleclemente.peoplemanagementmicroservice.infrastructure.dto.NewCustomerDTO;
import com.aleclemente.peoplemanagementmicroservice.infrastructure.repositories.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class CustomerControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private CustomerRepository customerRepository;

    @AfterEach
    void tearDown() {
        customerRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar um cliente")
    public void testCreate() throws Exception {

        var customer = new NewCustomerDTO(
                "Usuário 1",
                "12345678901",
                "2001-01-01",
                "12345678",
                "Street",
                "neighborhood",
                "city",
                "state");

        final var result = this.mvc.perform(
                        MockMvcRequestBuilders.post("/customers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(customer))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andReturn().getResponse().getContentAsByteArray();

        var actualResponse = mapper.readValue(result, NewCustomerDTO.class);
        Assertions.assertEquals(customer.name(), actualResponse.name());
        Assertions.assertEquals(customer.cpf(), actualResponse.cpf());
        Assertions.assertEquals(customer.dateOfBirth(), actualResponse.dateOfBirth());
        Assertions.assertEquals(customer.street(), actualResponse.street());
        Assertions.assertEquals(customer.neighborhood(), actualResponse.neighborhood());
        Assertions.assertEquals(customer.city(), actualResponse.city());
        Assertions.assertEquals(customer.state(), actualResponse.state());
        Assertions.assertEquals(customer.zipCode(), actualResponse.zipCode());
    }

    @Test
    @DisplayName("Não deve cadastrar um cliente com CPF duplicado")
    public void testCreateWithDuplicatedCPFShouldFail() throws Exception {

        var customer = new NewCustomerDTO(
                "Usuário 1",
                "12345678901",
                "2001-01-01",
                "12345678",
                "street",
                "neighborhood",
                "city",
                "state");

        // Cria o primeiro cliente
        this.mvc.perform(
                        MockMvcRequestBuilders.post("/customers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(customer))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andReturn().getResponse().getContentAsByteArray();
        customer = new NewCustomerDTO("Usuário 2", "12345678901", "02/02/2002", "12345678", "street", "neighborhood", "city", "state");

        // Tenta criar o segundo cliente com o mesmo CPF
        this.mvc.perform(
                        MockMvcRequestBuilders.post("/customers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(customer))
                )
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.content().string("Customer already exists"));
    }

    @Test
    @DisplayName("Deve obter um cliente por id")
    public void testGet() throws Exception {

        var customer = new NewCustomerDTO(
                "Usuário 1",
                "12345678901",
                "2001-01-01",
                "12345678",
                "street",
                "neighborhood",
                "city",
                "state");

        final var createResult = this.mvc.perform(
                        MockMvcRequestBuilders.post("/customers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(customer))
                )
                .andReturn().getResponse().getContentAsByteArray();

        var customerId = mapper.readValue(createResult, CreateCustomerUseCase.Output.class).id();

        final var result = this.mvc.perform(
                        MockMvcRequestBuilders.get("/customers/{id}", customerId)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsByteArray();

        var actualResponse = mapper.readValue(result, CreateCustomerUseCase.Output.class);

        Assertions.assertEquals(customerId, actualResponse.id());
        Assertions.assertEquals(customer.name(), actualResponse.name());
        Assertions.assertEquals(customer.cpf(), actualResponse.cpf());
        Assertions.assertEquals(LocalDate.parse(customer.dateOfBirth(), DateTimeFormatter.ISO_DATE), actualResponse.dateOfBirth());
        Assertions.assertEquals(customer.zipCode(), actualResponse.zipCode());
        Assertions.assertEquals(customer.street(), actualResponse.street());
        Assertions.assertEquals(customer.neighborhood(), actualResponse.neighborhood());
        Assertions.assertEquals(customer.city(), actualResponse.city());
        Assertions.assertEquals(customer.state(), actualResponse.state());
    }
}