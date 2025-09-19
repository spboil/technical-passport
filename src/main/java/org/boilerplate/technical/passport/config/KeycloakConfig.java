package org.boilerplate.technical.passport.config;

import java.util.Map;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.token.TokenService;
import org.keycloak.authorization.client.AuthzClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Configuration
@Getter
@RequiredArgsConstructor
public class KeycloakConfig {

    @Value("${keycloak.url}")
    private String keycloakUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.clientId}")
    private String clientId;

    @Value("${keycloak.clientUUID}")
    private String clientUUID;

    @Value("${keycloak.clientSecret}")
    private String clientSecret;

    private final ObjectMapper objectMapper;

    @Bean
    public Client resteasyClient() {
        ResteasyJackson2Provider provider = new ResteasyJackson2Provider();
        provider.setMapper(objectMapper.copy()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false));

        return ClientBuilder.newBuilder()
                .register(provider)
                .build();
    }

    @Bean
    public Keycloak keycloak(Client client) {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .resteasyClient((ResteasyClient) client)
                .build();
    }

    @Bean
    public AuthzClient authzClient() {
        org.keycloak.authorization.client.Configuration config = new org.keycloak.authorization.client.Configuration(
                keycloakUrl,
                realm,
                clientId,
                Map.of("secret", clientSecret),
                null);

        return AuthzClient.create(config);
    }

    @Bean
    public TokenService tokenService(Client client) {
        WebTarget target = client.target(keycloakUrl);
        return Keycloak.getClientProvider().targetProxy(target, TokenService.class);
    }
}
