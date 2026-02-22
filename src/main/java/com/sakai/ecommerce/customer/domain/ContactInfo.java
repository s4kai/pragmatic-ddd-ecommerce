package com.sakai.ecommerce.customer.domain;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class ContactInfo {
    private String email;
    private String phone;

    public ContactInfo(String email, String phone) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email inválido");
        }

        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Telefone obrigatório");
        }

        this.email = email;
        this.phone = phone;
    }
}
