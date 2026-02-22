package com.sakai.ecommerce.customer.domain;

import java.util.Optional;

public interface CustomerRepository {
    void save(Customer customer);
    Optional<Customer> findByDocument(String document);
}
