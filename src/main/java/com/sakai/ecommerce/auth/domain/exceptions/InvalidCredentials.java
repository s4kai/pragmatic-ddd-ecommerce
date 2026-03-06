package com.sakai.ecommerce.auth.domain.exceptions;

public class InvalidCredentials extends RuntimeException {
  public InvalidCredentials(String message) {
    super(message);
  }
}
