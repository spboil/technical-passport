package org.boilerplate.technical.passport.controller;

import org.boilerplate.technical.passport.iservice.IAuthService;
import org.boilerplate.technical.passport.payload.request.LoginRequest;
import org.boilerplate.technical.passport.payload.response.JwtResponse;
import org.boilerplate.technical.passport.payload.response.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticate(@RequestBody @Valid LoginRequest request){
        log.info("AuthController authenticate request: {}", request);
        JwtResponse response = authService.authenticate(request);
        log.info("AuthController authenticate ");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JwtResponse> refreshToken(@RequestParam(required = true) String refreshToken){
        log.info("AuthController.refreshToken request: {}", refreshToken);
        JwtResponse response = authService.refreshToken(refreshToken);
        log.info("AuthController.refreshToken response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/current-user")
    public ResponseEntity<UserResponse> currentUser(@AuthenticationPrincipal Jwt jwt){
        log.info("AuthController.currentUser request: {}", jwt.getTokenValue());
        UserResponse response = authService.currentUser(jwt);
        log.info("AuthController.currentUser response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestParam(required = true) String refreshToken){
        log.info("AuthController.logout request: {}", refreshToken);
        authService.logout(refreshToken);
        log.info("AuthController.logout response: void");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
