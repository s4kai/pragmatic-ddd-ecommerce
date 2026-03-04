package com.sakai.ecommerce.catalog.domain.exception;

import com.sakai.ecommerce.shared.domain.exception.BusinessError;

public class DuplicateSKUException extends BusinessError {
    public DuplicateSKUException() {
        super("Duplicate SKUs found in variants");
    }
}
