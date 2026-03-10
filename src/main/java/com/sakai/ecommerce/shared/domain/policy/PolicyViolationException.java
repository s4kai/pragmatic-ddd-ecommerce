package com.sakai.ecommerce.shared.domain.policy;

public class PolicyViolationException extends RuntimeException {
    public PolicyViolationException(String message) {
        super(message);
    }
}
