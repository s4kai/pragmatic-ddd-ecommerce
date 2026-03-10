package com.sakai.ecommerce.shared.domain.policy;

public class NotPolicy<T> implements Policy<T> {
    private final Policy<T> policy;
    
    public NotPolicy(Policy<T> policy) {
        this.policy = policy;
    }
    
    @Override
    public boolean isSatisfiedBy(T subject) {
        return !policy.isSatisfiedBy(subject);
    }
    
    @Override
    public String getViolationMessage() {
        return "NOT(" + policy.getViolationMessage() + ")";
    }
}
