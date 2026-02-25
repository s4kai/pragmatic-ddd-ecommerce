package com.sakai.ecommerce.customer.infra.controllers;

import com.sakai.ecommerce.customer.application.CustomerService;
import com.sakai.ecommerce.customer.infra.requests.CreateCustomerRequest;
import com.sakai.ecommerce.customer.infra.requests.mappers.CreateCustomerRequestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final CreateCustomerRequestMapper createCustomerRequestMapper;

    @PostMapping
    public ResponseEntity<UUID> create(@RequestBody @Valid CreateCustomerRequest command) {
        var customerDTO = createCustomerRequestMapper.toCommand(command);
        var createdCustomerId = customerService.create(customerDTO);

        return ResponseEntity
                .created(URI.create("/customers/" + createdCustomerId))
                .body(createdCustomerId);
    }
}
