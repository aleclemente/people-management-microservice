package com.aleclemente.peoplemanagementmicroservice.application.usecases;

import com.aleclemente.peoplemanagementmicroservice.application.UseCase;
import com.aleclemente.peoplemanagementmicroservice.infrastructure.services.CustomerService;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public class GetCustomerByIdUseCase
        extends UseCase<GetCustomerByIdUseCase.Input, Optional<GetCustomerByIdUseCase.Output>> {
    private final CustomerService customerService;

    public GetCustomerByIdUseCase(final CustomerService customerService) {
        this.customerService = Objects.requireNonNull(customerService);
    }

    @Override
    public Optional<Output> execute(final Input input) {
        return customerService.findById(input.id)
                .map(c -> new Output(
                        c.getId(),
                        c.getName(),
                        c.getCpf(),
                        c.getDateOfBirth(),
                        c.getZipCode(),
                        c.getStreet(),
                        c.getNeighborhood(),
                        c.getCity(),
                        c.getState()
                ));
    }

    public record Input(Long id){}
    public record Output(Long id,
                         String name,
                         String cpf,
                         LocalDate dateOfBirth,
                         String zipCode,
                         String street,
                         String neighborhood,
                         String city,
                         String state){}
}
