package com.aleclemente.peoplemanagementmicroservice.infrastructure.dto;

public record NewCustomerDTO(
        String name,
        String cpf,
        String dateOfBirth,
        String zipCode,
        String street,
        String neighborhood,
        String city,
        String state
) {
}
