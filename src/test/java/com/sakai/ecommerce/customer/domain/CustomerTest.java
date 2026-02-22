package com.sakai.ecommerce.customer.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void shouldCreateCustomerWithValidData() {
        var customer = new Customer(
                "12345678900",
                "João",
                "Silva",
                LocalDate.of(1990, 1, 1),
                validContactInfo(),
                validAddress()
        );

        assertNotNull(customer);
        assertNotNull(customer.getId());
        assertEquals("João", customer.getName());
    }

    @Test
    void shouldThrowExceptionWhenDocumentIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> new Customer(
                    null,
                    "João",
                    "Silva",
                    LocalDate.of(1990, 1, 1),
                    validContactInfo(),
                    validAddress()
            )
        );
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new Customer(
                        "12345678900",
                        null,
                        "Silva",
                        LocalDate.of(1990, 1, 1),
                        validContactInfo(),
                        validAddress()
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenLastNameIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> new Customer(
                    "12345678900",
                    "João",
                    null,
                    LocalDate.of(1990, 1, 1),
                    validContactInfo(),
                    validAddress()
            )
        );
    }

    @Test
    void shouldThrowExceptionWhenBirthDateIsInFuture() {
        assertThrows(IllegalArgumentException.class,
            () -> new Customer(
                    "12345678900",
                    "João",
                    "Silva",
                    LocalDate.now().plusDays(1),
                    validContactInfo(),
                    validAddress()
            )
        );
    }

    @Test
    void shouldThrowExceptionWhenContactInfoIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> new Customer(
                    "12345678900",
                    "João",
                    "Silva",
                    LocalDate.of(1990, 1, 1),
                    null,
                    validAddress()
            )
        );
    }

    @Test
    void shouldThrowExceptionWhenAddressIsNull() {
        assertThrows(IllegalArgumentException.class,
            () -> new Customer(
                    "12345678900",
                    "João",
                    "Silva",
                    LocalDate.of(1990, 1, 1),
                    validContactInfo(),
                    null
            )
        );
    }

    private ContactInfo validContactInfo() {
        return new ContactInfo("test@email.com", "11999999999");
    }

    private Address validAddress() {
        return new Address(
                "Rua A",
                "123",
                "Apto 1",
                "Centro",
                "São Paulo",
                "SP",
                "01000-000"
        );
    }
}
