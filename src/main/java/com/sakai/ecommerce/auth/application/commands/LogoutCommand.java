package com.sakai.ecommerce.auth.application.commands;

import java.util.UUID;

public record LogoutCommand(UUID userId) { }
