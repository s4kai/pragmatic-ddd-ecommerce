package com.sakai.ecommerce.customer.domain.exceptions;
import com.sakai.ecommerce.customer.domain.Customer;

public class CustomerAlreadyExists extends RuntimeException{
    public  CustomerAlreadyExists(Customer customer){
        super("Customer already exists");
    }
}
