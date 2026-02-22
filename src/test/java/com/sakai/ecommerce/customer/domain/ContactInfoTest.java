package com.sakai.ecommerce.customer.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContactInfoTest {

    @Test
    void shouldCreateContactInfoWithValidData() {
        var contactInfo = new ContactInfo("test@email.com", "11999999999");
        
        assertNotNull(contactInfo);
    }

    @Test
    void shouldThrowExceptionWhenEmailIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new ContactInfo(null, "11999999999"));
    }

    @Test
    void shouldThrowExceptionWhenEmailIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> new ContactInfo("invalidemail", "11999999999"));
    }

    @Test
    void shouldThrowExceptionWhenPhoneIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new ContactInfo("test@email.com", null));
    }

    @Test
    void shouldThrowExceptionWhenPhoneIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> new ContactInfo("test@email.com", ""));
    }
}
