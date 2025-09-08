package org.boilerplate.technical.passport.payload.request;

public record LoginRequest(
    String username,
    String password
) {
    
}
