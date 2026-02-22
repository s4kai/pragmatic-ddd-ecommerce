package com.sakai.ecommerce.customer.domain;

import com.sakai.ecommerce.customer.domain.events.CreatedCustomerEvent;
import com.sakai.ecommerce.shared.core.AggregateRoot;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
public class Customer extends AggregateRoot<UUID> {
    private String name;
    private String lastName;
    private LocalDate birthDate;
    private String document;

    @Embedded
    private ContactInfo contactInfo;

    @Embedded
    private Address address;

    public Customer(String document, String name, String lastName, LocalDate birthDate, ContactInfo contactInfo, Address address) {
        if(id == null) id = UUID.randomUUID();
        if(document == null || document.isBlank()) throw new IllegalArgumentException("CPF obrigatório");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Nome obrigatório");
        if (lastName == null || lastName.isBlank()) throw new IllegalArgumentException("Sobrenome obrigatório");
        if (birthDate == null || birthDate.isAfter(LocalDate.now())) throw new IllegalArgumentException("Data de nascimento inválida");
        if (contactInfo == null) throw new IllegalArgumentException("Informações de contato obrigatório");
        if (address == null) throw new IllegalArgumentException("Endereço obrigatório");

        this.document = document;
        this.name = name;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.contactInfo = contactInfo;
        this.address = address;

        this.registerEvent(new CreatedCustomerEvent(this.id));
    }
}
