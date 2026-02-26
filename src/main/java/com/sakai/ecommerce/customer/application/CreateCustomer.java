package com.sakai.ecommerce.customer.application;

import com.sakai.ecommerce.customer.application.commands.CreateCustomerCommand;
import com.sakai.ecommerce.customer.domain.Address;
import com.sakai.ecommerce.customer.domain.ContactInfo;
import com.sakai.ecommerce.customer.domain.Customer;
import com.sakai.ecommerce.customer.domain.CustomerRepository;
import com.sakai.ecommerce.customer.domain.exceptions.CustomerAlreadyExists;
import com.sakai.ecommerce.shared.application.dto.AddressDTO;
import com.sakai.ecommerce.shared.infra.services.DomainEventPublisher;
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
    public UUID handle(CreateCustomerCommand command) {
        if(customerRepository.findByDocument(command.getCpf()).isPresent()){
            throw new CustomerAlreadyExists();
        }

        var addressDTO = command.getAddress();
        var newCustomer = getCustomer(command, addressDTO);

        customerRepository.save(newCustomer);
        eventPublisher.publish(newCustomer);

        return newCustomer.getId();
    }

    private static @NonNull Customer getCustomer(CreateCustomerCommand command, AddressDTO addressDTO) {
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
                command.getCpf(),
                command.getName(),
                command.getLastName(),
                command.getBirthDate(),
                new ContactInfo(command.getEmail(), command.getPhone()),
                newAddress
        );
    }
}
