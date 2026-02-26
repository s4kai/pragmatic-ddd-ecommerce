package com.sakai.ecommerce.shared.application.dto;

import lombok.Data;

@Data
public class AddressDTO {
    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;
    private String zipCode;
}
