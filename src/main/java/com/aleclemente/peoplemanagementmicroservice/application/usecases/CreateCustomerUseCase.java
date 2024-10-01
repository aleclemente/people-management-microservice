package com.aleclemente.peoplemanagementmicroservice.application.usecases;

import com.aleclemente.peoplemanagementmicroservice.application.UseCase;
import com.aleclemente.peoplemanagementmicroservice.application.exception.ValidationException;
import com.aleclemente.peoplemanagementmicroservice.infrastructure.models.Customer;
import com.aleclemente.peoplemanagementmicroservice.infrastructure.services.CustomerService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CreateCustomerUseCase
        extends UseCase<CreateCustomerUseCase.Input, CreateCustomerUseCase.Output> {

    private final CustomerService customerService;

    public CreateCustomerUseCase(CustomerService customerService){
        this.customerService = customerService;
    }


    @Override
    public CreateCustomerUseCase.Output execute(final Input input) {
        if (customerService.findByCpf(input.cpf).isPresent()) {
            throw new ValidationException("Customer already exists");
        }

        var customer = new Customer();
        customer.setName(input.name);
        customer.setCpf(input.cpf);
        customer.setDateOfBirth(LocalDate.parse(input.dateOfBirth, DateTimeFormatter.ISO_DATE));
        customer.setZipCode(input.zipCode);
        customer.setStreet(input.street);
        customer.setNeighborhood(input.neighborhood);
        customer.setCity(input.city);
        customer.setState(input.state);

        customer = customerService.save(customer);

        return new Output(customer.getId(), customer.getName(), customer.getCpf(), customer.getDateOfBirth(), customer.getZipCode(), customer.getStreet(), customer.getNeighborhood(), customer.getCity(), customer.getState());
    }

    public record Input(String name, String cpf, String dateOfBirth, String zipCode, String street, String neighborhood, String city, String state){}

    public record Output(Long id, String name, String cpf, LocalDate dateOfBirth, String zipCode, String street, String neighborhood, String city, String state){}
}
