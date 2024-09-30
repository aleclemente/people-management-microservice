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
    private final GetCustomerByIdUseCase getCustomerByIdUseCase;

    public CustomerController(
            final CreateCustomerUseCase createCustomerUseCase,
            final GetCustomerByIdUseCase getCustomerByIdUseCase
    ) {
        this.createCustomerUseCase = Objects.requireNonNull(createCustomerUseCase);
        this.getCustomerByIdUseCase = Objects.requireNonNull(getCustomerByIdUseCase);
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

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        return getCustomerByIdUseCase.execute(new GetCustomerByIdUseCase.Input(id))
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }
}
