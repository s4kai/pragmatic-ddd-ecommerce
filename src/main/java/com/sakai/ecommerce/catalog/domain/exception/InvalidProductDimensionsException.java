package com.sakai.ecommerce.catalog.domain.exception;

import com.sakai.ecommerce.shared.domain.exception.BusinessError;

public class InvalidProductDimensionsException extends BusinessError {
    public InvalidProductDimensionsException(String message) {
        super("Invalid product dimensions: " + message);
    }
}
