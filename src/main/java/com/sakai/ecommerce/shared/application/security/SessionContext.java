package com.sakai.ecommerce.shared.application.security;

public interface SessionContext {
    String getCurrentSessionId();
    boolean hasActiveSession();
    void clearSession();
}
