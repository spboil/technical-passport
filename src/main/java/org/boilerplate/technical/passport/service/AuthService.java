package org.boilerplate.technical.passport.service;

import org.boilerplate.technical.passport.config.KeycloakConfig;
import org.boilerplate.technical.passport.exception.TechnicalException;
import org.boilerplate.technical.passport.iservice.IAuthService;
import org.boilerplate.technical.passport.iservice.IUserService;
import org.boilerplate.technical.passport.payload.request.LoginRequest;
import org.boilerplate.technical.passport.payload.request.UserCreateRequest;
import org.boilerplate.technical.passport.payload.response.JwtResponse;
import org.boilerplate.technical.passport.payload.response.UserResponse;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.token.TokenService;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import jakarta.ws.rs.core.Form;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public final class AuthService implements IAuthService{
    
    private final AuthzClient authzClient;
    private final TokenService tokenService;
    private final IUserService userService;
    private final KeycloakConfig keycloakConfig;

    @Override
    public JwtResponse authenticate(LoginRequest request) {
        log.info("Authenticating user: {}", request.username());
        try {
            AccessTokenResponse response = authzClient.obtainAccessToken(request.username(), request.password());
            log.info("User {} authenticated successfully", request.username());
            return new JwtResponse(
                response.getToken(),
                response.getRefreshToken()
            );
        } catch (Exception e) {
            log.error("Authentication failed for user: {}", request.username(), e);
            throw new TechnicalException("Invalid username or password", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public JwtResponse refreshToken(String refreshToken) {
        log.info("Refreshing token for refresh token: {}", refreshToken);
        try {
            Form form = new Form().param(OAuth2Constants.GRANT_TYPE, OAuth2Constants.REFRESH_TOKEN)
                .param(OAuth2Constants.REFRESH_TOKEN, refreshToken)
                .param(OAuth2Constants.CLIENT_ID, keycloakConfig.getClientId());
            AccessTokenResponse response = tokenService.refreshToken(
                keycloakConfig.getRealm(),
                form.asMap()
            );
            return new JwtResponse(response.getToken(), response.getRefreshToken());
        } catch (Exception e) {
            log.error("Error refreshing token", e);
            throw new TechnicalException("Token refresh failed", HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public void register(UserCreateRequest request) {
        log.info("Registering user: {}", request.username());
        userService.createUser(request);
        log.info("User {} registered successfully", request.username());
    }

    @Override
    public UserResponse currentUser(Jwt jwt) {
        log.info("Current user with accessToken: {}", jwt.getTokenValue());
        
        String id = jwt.getSubject();
        UserResponse user = userService.getUserById(id);
        if(user == null) {
            log.error("User not found with id: {}", id);
            throw new TechnicalException("User not found", HttpStatus.NOT_FOUND);
        }
        return user;
    }

    @Override
    public void logout(String refreshToken) {
        log.info("Logging out user with refresh token: {}", refreshToken);
        try {
            Form form = new Form()
                .param(OAuth2Constants.REFRESH_TOKEN, refreshToken)
                .param(OAuth2Constants.CLIENT_ID, keycloakConfig.getClientId());
            tokenService.logout(keycloakConfig.getRealm(), form.asMap());
            log.info("User logged out successfully");
        } catch (Exception e) {
            log.error("Error during logout", e);
            throw new TechnicalException("Logout failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
}
