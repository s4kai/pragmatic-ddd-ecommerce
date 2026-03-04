package com.sakai.ecommerce.customer.infra.requests;

import com.sakai.ecommerce.shared.application.dto.AddressDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateCustomerRequest {
    @NotNull(message = "CPF cannot be null")
    private String cpf;

    @NotNull(message = "Name cannot be null")
    private String name;

    @NotNull(message = "lastName cannot be null")
    private String lastName;

    private LocalDate birthDate;

    @NotNull(message = "Email cannot be null")
    private String email;

    private String phone;
    private AddressDTO address;
}
