package com.sakai.ecommerce.customer.application;

import com.sakai.ecommerce.customer.application.commands.CreateCustomerCommand;
import com.sakai.ecommerce.customer.domain.Customer;
import com.sakai.ecommerce.customer.domain.CustomerRepository;
import com.sakai.ecommerce.customer.domain.exceptions.CustomerAlreadyExists;
import com.sakai.ecommerce.shared.dto.AddressDTO;
import com.sakai.ecommerce.shared.infra.DomainEventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCustomerTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private DomainEventPublisher eventPublisher;
    @InjectMocks
    private CreateCustomer createCustomer;

    @Test
    void shouldCreateCustomerSuccessfully() {
        var command = validCustomerCommand();
        when(customerRepository.findByDocument(command.getCpf())).thenReturn(Optional.empty());

        var customerId = createCustomer.handle(command);

        assertNotNull(customerId);
        verify(customerRepository).save(any(Customer.class));
        verify(eventPublisher).publish(any(Customer.class));
    }

    @Test
    void shouldThrowExceptionWhenCustomerAlreadyExists() {
        var command = validCustomerCommand();
        when(customerRepository.findByDocument(command.getCpf())).thenReturn(Optional.of(mock(Customer.class)));

        assertThrows(CustomerAlreadyExists.class, () -> createCustomer.handle(command));
        
        verify(customerRepository, never()).save(any());
    }

    private CreateCustomerCommand validCustomerCommand() {
        var command = new CreateCustomerCommand();
        command.setName("João");
        command.setLastName("Silva");
        command.setBirthDate(LocalDate.of(1990, 1, 1));
        command.setEmail("test@email.com");
        command.setPhone("11999999999");
        command.setCpf("12345678900");
        command.setAddress(validAddressDTO());
        return command;
    }

    private AddressDTO validAddressDTO() {
        var address = new AddressDTO();
        address.setStreet("Rua A");
        address.setNumber("123");
        address.setComplement("Apto 1");
        address.setNeighborhood("Centro");
        address.setCity("São Paulo");
        address.setState("SP");
        address.setZipCode("01000-000");
        return address;
    }
}
