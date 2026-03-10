package com.sakai.ecommerce.auth.application.services;

import com.sakai.ecommerce.shared.application.security.AuthorizationContext;
import com.sakai.ecommerce.shared.domain.policy.Policy;
import com.sakai.ecommerce.shared.domain.policy.PolicyViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PolicyEnforcer {
    private final AuthorizationContext authorizationContext;
    
    public void enforce(Policy<AuthorizationContext> policy) {
        if (!policy.isSatisfiedBy(authorizationContext)) {
            throw new PolicyViolationException(policy.getViolationMessage());
        }
    }
    
    public boolean check(Policy<AuthorizationContext> policy) {
        return policy.isSatisfiedBy(authorizationContext);
    }
}
