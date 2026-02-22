package com.sakai.ecommerce.customer.infra.repository.jpa;


import com.sakai.ecommerce.customer.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaCustomerRepository extends JpaRepository<Customer, UUID> {
    Optional<Customer> findByDocument(String document);
}
