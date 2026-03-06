package com.sakai.ecommerce.auth.domain.exceptions;

import com.sakai.ecommerce.shared.domain.exception.BusinessError;

public class UserExists extends BusinessError {
    public UserExists() {
        super("User with this email already exists");
    }
}
