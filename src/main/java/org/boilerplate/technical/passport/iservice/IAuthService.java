package org.boilerplate.technical.passport.iservice;

import org.boilerplate.technical.passport.payload.request.LoginRequest;
import org.boilerplate.technical.passport.payload.request.UserCreateRequest;
import org.boilerplate.technical.passport.payload.response.JwtResponse;
import org.boilerplate.technical.passport.payload.response.UserResponse;
import org.springframework.security.oauth2.jwt.Jwt;

public interface IAuthService {
    JwtResponse authenticate(LoginRequest request);
    void register(UserCreateRequest request);
    UserResponse currentUser(Jwt jwt);
    void logout();
    
}
