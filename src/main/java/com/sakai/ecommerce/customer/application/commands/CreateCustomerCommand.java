package com.sakai.ecommerce.customer.application.commands;

import com.sakai.ecommerce.shared.dto.AddressDTO;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateCustomerCommand {
    private String name;
    private String lastName;
    private LocalDate birthDate;

    private String email;
    private String phone;
    private String cpf;
    private AddressDTO address;
}
