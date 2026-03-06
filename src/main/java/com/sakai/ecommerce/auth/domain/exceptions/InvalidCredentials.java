package com.sakai.ecommerce.auth.domain.exceptions;

import com.sakai.ecommerce.shared.domain.exception.BusinessError;

public class InvalidCredentials extends BusinessError {
    public InvalidCredentials() {
        super("Invalid Credentials");
    }
}
