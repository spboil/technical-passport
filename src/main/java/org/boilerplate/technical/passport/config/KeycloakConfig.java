package org.boilerplate.technical.passport.config;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.ws.rs.client.ClientBuilder;
import lombok.Getter;

@Configuration
@Getter
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

    @Bean
    Keycloak keycloak(){
        ResteasyJackson2Provider provider = new ResteasyJackson2Provider();
        provider.setMapper(
            new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            );

        ResteasyClient client = (ResteasyClient) ClientBuilder.newBuilder()
            .register(provider)
            .build();
        
        return KeycloakBuilder.builder()
            .serverUrl(keycloakUrl)
            .realm(realm)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
            .resteasyClient(client)
            .build();
    }
}
