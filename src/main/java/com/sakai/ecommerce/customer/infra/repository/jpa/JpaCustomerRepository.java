package com.sakai.ecommerce.customer.infra.repository;


import com.sakai.ecommerce.customer.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaCustomerRepository extends JpaRepository<Customer, UUID> {
}
