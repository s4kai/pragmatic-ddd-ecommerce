package com.sakai.ecommerce.auth.domain.exceptions;

import com.sakai.ecommerce.shared.domain.exception.BusinessError;

public class InvalidVerifyEmailToken  extends BusinessError {
    public InvalidVerifyEmailToken() {
        super("Your token is invalid");
    }
}