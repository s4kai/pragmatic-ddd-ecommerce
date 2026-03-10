package com.sakai.ecommerce.shared.infra;

import com.sakai.ecommerce.shared.application.security.AuthenticationContext;
import com.sakai.ecommerce.shared.application.security.SessionContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@RequiredArgsConstructor
public class SpringSessionContext implements SessionContext {

    private final AuthenticationContext context;

    @Override
    public String getCurrentSessionId() {
        var attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }

        var session = attributes.getRequest().getSession(false);
        if (session != null) {
            return session.getId();
        }

        if (!context.isAuthenticated()) {
            session = attributes.getRequest().getSession(true);
            return session.getId();
        }

        return null;
    }

    @Override
    public boolean hasActiveSession() {
        var attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return false;
        }
        
        return attributes.getRequest().getSession(false) != null;
    }

    @Override
    public void clearSession() {
        var attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            var session = attributes.getRequest().getSession(false);
            if (session != null) {
                session.invalidate();
            }
        }
    }
}
