package com.aleclemente.peoplemanagementmicroservice.application.usecases;

import com.aleclemente.peoplemanagementmicroservice.IntegrationTest;
import com.aleclemente.peoplemanagementmicroservice.application.exception.ValidationException;
import com.aleclemente.peoplemanagementmicroservice.infrastructure.models.Customer;
import com.aleclemente.peoplemanagementmicroservice.infrastructure.repositories.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class CreateCustomerUseCaseTestIT extends IntegrationTest {
    @Autowired
    private CreateCustomerUseCase useCase;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void tearDown(){
        customerRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar um cliente")
    public void testCreateCustomer() {
        //given
        final var expectedName = "Usuário 1";
        final var expectedCPF = "12345678901";
        final var expectedDateOfBirth = "2001-01-01";
        final var expectedZipCode = "12345678";
        final var expectedStreet = "Street";
        final var expectedNeighborhood = "neighborhood";
        final var expectedCity = "city";
        final var expectedState = "state";

        final var createInput = new CreateCustomerUseCase.Input(expectedName, expectedCPF, expectedDateOfBirth, expectedZipCode, expectedStreet, expectedNeighborhood, expectedCity, expectedState);

        //when
        final var output = useCase.execute(createInput);

        //then
        Assertions.assertNotNull(output.id());
        Assertions.assertEquals(expectedName, output.name());
        Assertions.assertEquals(expectedCPF, output.cpf());
        Assertions.assertEquals(LocalDate.parse(expectedDateOfBirth, DateTimeFormatter.ISO_DATE), output.dateOfBirth());
        Assertions.assertEquals(expectedZipCode, output.zipCode());
        Assertions.assertEquals(expectedStreet, output.street());
        Assertions.assertEquals(expectedNeighborhood, output.neighborhood());
        Assertions.assertEquals(expectedCity, output.city());
        Assertions.assertEquals(expectedState, output.state());


    }

    @Test
    @DisplayName("Não deve cadastrar um cliente com CPF duplicado")
    public void testCreateCustomerWithDuplicatedCPFShouldFail() {
        //given
        final var expectedName = "Usuário 1";
        final var expectedCPF = "12345678901";
        final var expectedDateOfBirth = "2001-01-01";
        final var expectedZipCode = "12345678";
        final var expectedStreet = "Street";
        final var expectedNeighborhood = "neighborhood";
        final var expectedCity = "city";
        final var expectedState = "state";
        final var expectedError = "Customer already exists";

        createCustomer(expectedName, expectedCPF, expectedDateOfBirth, expectedZipCode, expectedStreet, expectedNeighborhood, expectedCity, expectedState);

        final var createInput = new CreateCustomerUseCase.Input(expectedName, expectedCPF, expectedDateOfBirth, expectedZipCode, expectedStreet, expectedNeighborhood, expectedCity, expectedState);

        //when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        //then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve cadastrar um cliente menor de idade")
    public void testCreateUnderAgeCustomerShouldFail() {
        //given
        final var expectedName = "Usuário 1";
        final var expectedCPF = "12345678901";
        final var expectedDateOfBirth = "2020-01-01";
        final var expectedZipCode = "12345678";
        final var expectedStreet = "Street";
        final var expectedNeighborhood = "neighborhood";
        final var expectedCity = "city";
        final var expectedState = "state";
        final var expectedError = "dateOfBirth must be equal or greater than 18 years old!";

        final var createInput = new CreateCustomerUseCase.Input(expectedName, expectedCPF, expectedDateOfBirth, expectedZipCode, expectedStreet, expectedNeighborhood, expectedCity, expectedState);

        //when
        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        //then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    private Customer createCustomer(final String name, final String cpf, final String dateOfBirth, final String zipCode, final String street, final String neighborhood, final String city, final String state){
        final var aCustomer = new Customer();
        aCustomer.setName(name);
        aCustomer.setCpf(cpf);
        aCustomer.setDateOfBirth(LocalDate.parse(dateOfBirth));
        aCustomer.setZipCode(zipCode);
        aCustomer.setStreet(street);
        aCustomer.setNeighborhood(neighborhood);
        aCustomer.setCity(city);
        aCustomer.setState(state);
        return customerRepository.save(aCustomer);
    }
}