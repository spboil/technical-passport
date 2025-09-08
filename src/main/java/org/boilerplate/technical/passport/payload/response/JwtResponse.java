package org.boilerplate.technical.passport.payload.response;

public record JwtResponse(
    String accessToken,
    String refreshToken
) {
    
}