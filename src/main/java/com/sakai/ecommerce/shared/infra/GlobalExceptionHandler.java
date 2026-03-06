package com.sakai.ecommerce.shared.infra;

import com.sakai.ecommerce.auth.domain.exceptions.InvalidCredentials;
import com.sakai.ecommerce.auth.domain.exceptions.UnauthorizedException;
import com.sakai.ecommerce.auth.domain.exceptions.UserExists;
import com.sakai.ecommerce.shared.domain.exception.BusinessError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessError.class)
    public ResponseEntity<Map<String, String>> handleBusinessError(BusinessError ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(InvalidCredentials.class)
    public ResponseEntity<Map<String, String>> handleInvalidCredentials(InvalidCredentials ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorized(UnauthorizedException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(UserExists.class)
    public ResponseEntity<Map<String, String>> handleUserExists(UserExists ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericError(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Erro interno do servidor"));
    }
}
