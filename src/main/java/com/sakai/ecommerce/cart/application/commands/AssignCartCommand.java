package com.sakai.ecommerce.cart.application.commands;

import java.util.UUID;

public record AssignCartCommand(
        UUID customerId,
        String sessionId
) {}
