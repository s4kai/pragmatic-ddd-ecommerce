package com.sakai.ecommerce.auth.application.commands;

public record RegisterUserCommand(
    String email,
    String password
) {}
