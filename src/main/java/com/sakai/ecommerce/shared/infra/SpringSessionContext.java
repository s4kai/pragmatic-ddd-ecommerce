package com.sakai.ecommerce.shared.infra;

import com.sakai.ecommerce.shared.application.security.SessionContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class SpringSessionContext implements SessionContext {

    @Override
    public String getCurrentSessionId() {
        var attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        
        var session = attributes.getRequest().getSession(false);
        if (session == null) {
            return null;
        }
        
        return session.getId();
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
