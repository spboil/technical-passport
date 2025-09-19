package org.boilerplate.technical.passport.payload.response;

public record UserResponse(
    String id,
    String username,
    String email,
    String firstName,
    String lastName
) {
    
}
