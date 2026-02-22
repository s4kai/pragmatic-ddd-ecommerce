package com.sakai.ecommerce.customer.application;

import com.sakai.ecommerce.customer.application.dto.CustomerDTO;
import com.sakai.ecommerce.customer.domain.Address;
import com.sakai.ecommerce.customer.domain.ContactInfo;
import com.sakai.ecommerce.customer.domain.Customer;
import com.sakai.ecommerce.customer.domain.CustomerRepository;
import com.sakai.ecommerce.shared.dto.AddressDTO;
import com.sakai.ecommerce.shared.infra.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
class CreateCustomer {
    private final CustomerRepository customerRepository;
    private final DomainEventPublisher eventPublisher;

    @Transactional
    public UUID handle(CustomerDTO dto) {
        customerRepository.findByDocument(dto.getCpf())
            .ifPresent(customer -> {
                throw new RuntimeException("Customer already exists");
            });

        var addressDTO = dto.getAddress();
        var newCustomer = getCustomer(dto, addressDTO);

        customerRepository.save(newCustomer);
        eventPublisher.publish(newCustomer);

        return newCustomer.getId();
    }

    private static @NonNull Customer getCustomer(CustomerDTO dto, AddressDTO addressDTO) {
        var newAddress = new Address(
                addressDTO.getStreet(),
                addressDTO.getNumber(),
                addressDTO.getComplement(),
                addressDTO.getNeighborhood(),
                addressDTO.getCity(),
                addressDTO.getState(),
                addressDTO.getZipCode()
        );

        return new Customer(
                dto.getCpf(),
                dto.getName(),
                dto.getLastName(),
                dto.getBirthDate(),
                new ContactInfo(dto.getEmail(), dto.getPhone()),
                newAddress
        );
    }
}
