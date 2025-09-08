package org.boilerplate.technical.passport.handler;

import org.boilerplate.technical.passport.exception.TechnicalException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(TechnicalException.class)
    public ResponseEntity<String> handleTechnicalException(TechnicalException ex) {
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }
}
