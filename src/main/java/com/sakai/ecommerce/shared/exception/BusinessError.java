package com.sakai.ecommerce.shared.exception;

public class BusinessError extends DomainError {
    public BusinessError(String message) {
        super(message);
    }

    public BusinessError(String message, Throwable throwable) {
        super(message, throwable);
    }
}
