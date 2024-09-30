package com.aleclemente.peoplemanagementmicroservice.infrastructure.configurations;

import com.aleclemente.peoplemanagementmicroservice.application.usecases.CreateCustomerUseCase;
import com.aleclemente.peoplemanagementmicroservice.infrastructure.services.CustomerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class UseCaseConfig {

    private final CustomerService customerService;

    public UseCaseConfig(
            final CustomerService customerService
    ) {
        this.customerService = Objects.requireNonNull(customerService);
    }

    @Bean
    public CreateCustomerUseCase createCustomerUseCase(){
        return new CreateCustomerUseCase(customerService);
    }
}
