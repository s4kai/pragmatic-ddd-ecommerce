package com.sakai.ecommerce.customer.domain.exceptions;

public class CustomerAlreadyExists extends RuntimeException{
    public  CustomerAlreadyExists(){
        super("Customer already exists");
    }
}
