package com.sakai.ecommerce.customer.application;

import com.sakai.ecommerce.customer.application.commands.CreateCustomerCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CreateCustomer createCustomer;

    public UUID create(CreateCustomerCommand command) {
        return createCustomer.handle(command);
    }
}
