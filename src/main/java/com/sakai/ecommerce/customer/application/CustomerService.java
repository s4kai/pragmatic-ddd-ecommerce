package com.sakai.ecommerce.customer.application;

import com.sakai.ecommerce.customer.application.dto.CustomerDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CreateCustomer createCustomer;

    public UUID create(CustomerDTO customerDTO) {
        return createCustomer.handle(customerDTO);
    }
}
