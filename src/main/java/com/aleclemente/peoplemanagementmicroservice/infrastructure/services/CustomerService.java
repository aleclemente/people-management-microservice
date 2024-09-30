package com.aleclemente.peoplemanagementmicroservice.infrastructure.services;

import com.aleclemente.peoplemanagementmicroservice.infrastructure.models.Customer;
import com.aleclemente.peoplemanagementmicroservice.infrastructure.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class CustomerService
{

    @Autowired
    private CustomerRepository repository;

    @Transactional
    public Customer save(Customer customer) {
        return repository.save(customer);
    }

    public Optional<Customer> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<Customer> findByCpf(String cpf) {
        return repository.findByCpf(cpf);
    }
}
