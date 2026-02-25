package com.sakai.ecommerce.customer.infra.requests.mappers;

import com.sakai.ecommerce.customer.application.commands.CreateCustomerCommand;
import com.sakai.ecommerce.customer.infra.requests.CreateCustomerRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreateCustomerRequestMapper {
    CreateCustomerCommand toCommand(CreateCustomerRequest request);
}