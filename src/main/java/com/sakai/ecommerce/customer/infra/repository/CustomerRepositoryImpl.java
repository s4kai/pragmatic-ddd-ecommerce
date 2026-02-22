package com.sakai.ecommerce.customer.infra.repository;

import com.sakai.ecommerce.customer.domain.Customer;
import com.sakai.ecommerce.customer.domain.CustomerRepository;
import com.sakai.ecommerce.customer.infra.repository.jpa.JpaCustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {
    private final JpaCustomerRepository jpaRepository;

    public void save(Customer customer) {
        jpaRepository.save(customer);
    }

    public Optional<Customer> findByDocument(String document) {
        return jpaRepository.findByDocument(document);
    }
}
