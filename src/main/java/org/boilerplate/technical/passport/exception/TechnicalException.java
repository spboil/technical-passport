package org.boilerplate.technical.passport.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class TechnicalException extends RuntimeException{
    private final HttpStatus status;

    public TechnicalException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
