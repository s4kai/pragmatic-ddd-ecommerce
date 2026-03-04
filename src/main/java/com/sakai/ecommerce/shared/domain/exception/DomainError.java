package com.sakai.ecommerce.shared.domain.exception;

public class DomainError extends RuntimeException{
    public DomainError(String message) {
        super(message);
    }

    public DomainError(String message, Throwable throwable) {
        super(message, throwable);
    }
}
