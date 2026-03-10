package com.sakai.ecommerce.shared.domain.policy;

import java.util.ArrayList;
import java.util.List;

public class CompositePolicy<T> implements Policy<T> {
    private final List<Policy<T>> policies = new ArrayList<>();
    private final LogicalOperator operator;
    
    public enum LogicalOperator { AND, OR }
    
    public CompositePolicy(LogicalOperator operator) {
        this.operator = operator;
    }
    
    public CompositePolicy<T> add(Policy<T> policy) {
        policies.add(policy);
        return this;
    }
    
    @Override
    public boolean isSatisfiedBy(T subject) {
        if (operator == LogicalOperator.AND) {
            return policies.stream().allMatch(p -> p.isSatisfiedBy(subject));
        }
        return policies.stream().anyMatch(p -> p.isSatisfiedBy(subject));
    }
    
    @Override
    public String getViolationMessage() {
        if (operator == LogicalOperator.AND) {
            return "All policies must be satisfied: " + 
                policies.stream()
                    .map(Policy::getViolationMessage)
                    .reduce((a, b) -> a + "; " + b)
                    .orElse("No policies defined");
        }
        return "At least one policy must be satisfied: " + 
            policies.stream()
                .map(Policy::getViolationMessage)
                .reduce((a, b) -> a + " OR " + b)
                .orElse("No policies defined");
    }
}
