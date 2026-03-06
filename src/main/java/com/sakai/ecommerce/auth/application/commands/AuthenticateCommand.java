package com.sakai.ecommerce.auth.application.commands;

public record AuthenticateCommand(
    String email,
    String password
) {}
