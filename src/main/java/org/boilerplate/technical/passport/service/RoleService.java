package org.boilerplate.technical.passport.service;

import java.util.List;

import org.boilerplate.technical.passport.config.KeycloakConfig;
import org.boilerplate.technical.passport.exception.TechnicalException;
import org.boilerplate.technical.passport.iservice.IRoleService;
import org.boilerplate.technical.passport.payload.mapper.RoleMapper;
import org.boilerplate.technical.passport.payload.request.RoleRequest;
import org.boilerplate.technical.passport.payload.response.RoleResponse;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public final class RoleService implements IRoleService{
    private final KeycloakConfig keycloakConfig;
    private final Keycloak keycloak;

    @Override
    public RoleResponse createRole(RoleRequest request) {
        log.info("Creating role with name: {}", request.name());
        try {
            keycloak.realm(keycloakConfig.getRealm())
                .roles()
                .create(RoleMapper.toRoleRepresentation(request));
            log.info("Role created successfully with name: {}", request.name());
            return getRole(request.name());
        } catch (Exception e) {
            log.error("Error creating role with name: {}", request.name(), e);
            throw new TechnicalException("Error creating role", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @Override
    public void deleteRole(String name) {
        log.info("Deleting role with name: {}", name);
        try {
            keycloak.realm(keycloakConfig.getRealm())
                .roles()
                .deleteRole(name);
            log.info("Role deleted successfully with name: {}", name);
        } catch (Exception e) {
            log.error("Error deleting role with name: {}", name, e);
            throw new TechnicalException("Error deleting role", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Override
    public RoleResponse getRole(String name) {
        log.info("Fetching role with name: {}", name);
        try {
            RoleRepresentation roleRepresentation = keycloak
                .realm(keycloakConfig.getRealm())
                .roles()
                .get(name)
                .toRepresentation();
            log.info("Role fetched successfully with name: {}", name);
            return RoleMapper.toRoleResponse(roleRepresentation);
        } catch (Exception e) {
            log.error("Error fetching role with name: {}", name, e);
            throw new TechnicalException("Error fetching role", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Override
    public List<RoleResponse> getAllRoles(int page, int size) {
        log.info("Fetching all roles with page: {} and size: {}", page, size);
        try {
            List<RoleRepresentation> roles = keycloak
                .realm(keycloakConfig.getRealm())
                .clients()
                .get(keycloakConfig.getClientId())
                .roles()
                .list(page, size);
            log.info("Roles fetched successfully with page: {} and size: {}", page, size);
            return roles.stream()
                .map(RoleMapper::toRoleResponse)
                .toList();
        } catch (Exception e) {
            log.error("Error fetching roles with page: {} and size: {}", page, size, e);
            throw new TechnicalException("Error fetching roles", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
