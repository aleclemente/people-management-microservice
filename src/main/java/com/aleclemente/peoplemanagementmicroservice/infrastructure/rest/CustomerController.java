package com.aleclemente.peoplemanagementmicroservice.infrastructure.rest;

import com.aleclemente.peoplemanagementmicroservice.application.exception.ValidationException;
import com.aleclemente.peoplemanagementmicroservice.application.usecases.CreateCustomerUseCase;
import com.aleclemente.peoplemanagementmicroservice.application.usecases.GetCustomerByIdUseCase;
import com.aleclemente.peoplemanagementmicroservice.infrastructure.dto.NewCustomerDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Objects;

@RestController
@RequestMapping(value = "customers")
public class CustomerController {

    private final CreateCustomerUseCase createCustomerUseCase;

    public CustomerController(
            final CreateCustomerUseCase createCustomerUseCase
    ) {
        this.createCustomerUseCase = Objects.requireNonNull(createCustomerUseCase);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody NewCustomerDTO dto) {
        try{
            final var output = createCustomerUseCase.execute(new CreateCustomerUseCase.Input(dto.name(), dto.cpf(),dto.dateOfBirth(), dto.zipCode(), dto.street(), dto.neighborhood(), dto.city(), dto.state()));
            return ResponseEntity.created(URI.create("/customers/" + output.id())).body(output);
        }catch (ValidationException ex){
            return ResponseEntity.unprocessableEntity().body(ex.getMessage());
        }
    }
}
