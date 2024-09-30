package com.aleclemente.peoplemanagementmicroservice.infrastructure.repositories;

import com.aleclemente.peoplemanagementmicroservice.infrastructure.models.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

    Optional<Customer> findByCpf(String cpf);
}
