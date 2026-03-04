package com.sakai.ecommerce.cart.application.dto;

import java.util.UUID;

public record CartCheckoutResponse(
        UUID cartId
) {}
