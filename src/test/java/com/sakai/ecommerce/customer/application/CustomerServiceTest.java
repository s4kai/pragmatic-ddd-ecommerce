package com.sakai.ecommerce.customer.application;

import com.sakai.ecommerce.customer.application.dto.CustomerDTO;
import com.sakai.ecommerce.shared.dto.AddressDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CreateCustomer createCustomer;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void shouldCreateCustomer() {
        var dto = validCustomerDTO();
        var expectedId = UUID.randomUUID();
        when(createCustomer.handle(any(CustomerDTO.class))).thenReturn(expectedId);

        var result = customerService.create(dto);

        assertEquals(expectedId, result);
        verify(createCustomer).handle(dto);
    }

    private CustomerDTO validCustomerDTO() {
        var dto = new CustomerDTO();
        dto.setName("João");
        dto.setLastName("Silva");
        dto.setBirthDate(LocalDate.of(1990, 1, 1));
        dto.setEmail("test@email.com");
        dto.setPhone("11999999999");
        dto.setCpf("12345678900");
        dto.setAddress(validAddressDTO());
        return dto;
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
