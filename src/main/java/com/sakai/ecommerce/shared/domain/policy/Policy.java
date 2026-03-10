package com.sakai.ecommerce.shared.domain.policy;

public interface Policy<T> {
    boolean isSatisfiedBy(T subject);
    String getViolationMessage();
}
