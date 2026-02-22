package com.sakai.ecommerce.customer.application.dto;

import com.sakai.ecommerce.shared.dto.AddressDTO;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CustomerDTO {
    private String name;
    private String lastName;
    private LocalDate birthDate;

    private String email;
    private String phone;
    private String cpf;
    private AddressDTO address;
}
