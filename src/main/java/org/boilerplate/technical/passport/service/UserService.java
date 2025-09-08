package org.boilerplate.technical.passport.service;

import java.util.List;
import java.util.stream.Collectors;

import org.boilerplate.technical.passport.config.KeycloakConfig;
import org.boilerplate.technical.passport.exception.TechnicalException;
import org.boilerplate.technical.passport.iservice.IUserService;
import org.boilerplate.technical.passport.payload.mapper.RoleMapper;
import org.boilerplate.technical.passport.payload.mapper.UserMapper;
import org.boilerplate.technical.passport.payload.request.AssignRolesRequest;
import org.boilerplate.technical.passport.payload.request.RemoveRolesRequest;
import org.boilerplate.technical.passport.payload.request.UpdatePasswordRequest;
import org.boilerplate.technical.passport.payload.request.UserCreateRequest;
import org.boilerplate.technical.passport.payload.request.UserUpdateRequest;
import org.boilerplate.technical.passport.payload.response.RoleResponse;
import org.boilerplate.technical.passport.payload.response.UserResponse;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public final class UserService implements IUserService{

    private final Keycloak keycloak;
    private final KeycloakConfig keycloakConfig;

    @Override
    public UserResponse createUser(UserCreateRequest request) {
        log.info("Creating user with username: {}", request.username());
        UserRepresentation user = UserMapper.toUserRepresentation(request);

        try {
            Response response = keycloak
                .realm(keycloakConfig.getRealm())
                .users()
                .create(user);
            
            if(response.getStatus() == 400) {
                log.error("Failed to create user. Status: {}, Response: {}", response.getStatus(), response.readEntity(String.class));
                throw new TechnicalException("Failed to create user", HttpStatus.BAD_REQUEST);
            } else if(response.getStatus() == 403){
                log.error("Access denied when creating user. Status: {}, Response: {}", response.getStatus(), response.readEntity(String.class));
                throw new TechnicalException("", HttpStatus.FORBIDDEN);
            } else if(response.getStatus() == 409){
                log.error("User already exists. Status: {}, Response: {}", response.getStatus(), response.readEntity(String.class));
                throw new TechnicalException("User already exists", HttpStatus.CONFLICT);
            } else if(response.getStatus() == 500){
                log.error("Internal server error when creating user. Status: {}, Response: {}", response.getStatus(), response.readEntity(String.class));
                throw new TechnicalException("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
            log.info("User created with ID: {}", userId);
            return UserMapper.fromUserRepresentation(
                keycloak
                    .realm(keycloakConfig.getRealm())
                    .users()
                    .get(userId).toRepresentation()
                );
        } catch (Exception e) {
            log.error("Exception occurred while creating user: {}", e.getMessage());
            throw new TechnicalException("Exception occurred while creating user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public UserResponse updateUser(String id, UserUpdateRequest request) {
        log.info("Updating user with ID: {}", id);
        try {
            keycloak.realm(keycloakConfig.getRealm())
                .users()
                .get(id)
                .update(UserMapper.toUserRepresentation(request));
            log.info("User with ID: {} updated successfully", id);
            UserRepresentation updatedUser = keycloak
                .realm(keycloakConfig.getRealm())
                .users()
                .get(id)
                .toRepresentation();
            return UserMapper.fromUserRepresentation(updatedUser);
        } catch (Exception e) {
            log.error("Exception occurred while updating user: {}", e.getMessage());
            throw new TechnicalException("Exception occurred while updating user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteUser(String id) {
        log.info("Deleting user with ID: {}", id);
        try {
            Response response = keycloak
                .realm(keycloakConfig.getRealm())
                .users()
                .delete(id);
            if(response.getStatus() == 400){
                log.error("Failed to delete user. Status: {}, Response: {}", response.getStatus(), response.readEntity(String.class));
                throw new TechnicalException("Failed to delete user", HttpStatus.BAD_REQUEST);
            } else if(response.getStatus() == 403){
                log.error("Access denied when deleting user. Status: {}, Response: {}", response.getStatus(), response.readEntity(String.class));
                throw new TechnicalException("Failed to delete user", HttpStatus.FORBIDDEN);
            }
            log.info("User with ID: {} deleted successfully", id);
        } catch (Exception e) {
            log.error("Exception occurred while deleting user: {}", e.getMessage());
            throw new TechnicalException("Exception occurred while deleting user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public UserResponse getUserById(String id) {
        log.info("Fetching user with ID: {}", id);
        try {
            UserRepresentation user = keycloak
                .realm(keycloakConfig.getRealm())
                .users()
                .get(id)
                .toRepresentation();
            log.info("User with ID: {} fetched successfully", id);
            return UserMapper.fromUserRepresentation(user);
        } catch (Exception e) {
            log.error("Exception occurred while fetching user: {}", e.getMessage());
            throw new TechnicalException("Exception occurred while fetching user", HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public List<UserResponse> getAllUsers(int page, int size) {
        log.info("Fetching all users. Page: {}, Size: {}", page, size);
        try {
            List<UserRepresentation> users = keycloak
                .realm(keycloakConfig.getRealm())
                .users()
                .list(page, size);
            log.info("Fetched {} users", users.size());
            return users.stream()
                .map(UserMapper::fromUserRepresentation)
                .toList();
        } catch (Exception e) {
            log.error("Exception occurred while fetching users: {}", e.getMessage());
            throw new TechnicalException("Exception occurred while fetching users", HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public List<RoleResponse> getUserRoles(String id) {
        log.info("Fetching roles for user with ID: {}", id);
        try {
            List<RoleRepresentation> roles = keycloak
                .realm(keycloakConfig.getRealm())
                .users()
                .get(id)
                .roles()
                .clientLevel(keycloakConfig.getClientUUID())
                .listAll();
            log.info("Fetched {} roles for user with ID: {}", roles.size(), id);
            return roles.stream()
                .map(RoleMapper::toRoleResponse)
                .toList();
        } catch (Exception e) {
            log.error("Exception occurred while fetching user roles: {}", e.getMessage());
            throw new TechnicalException("Exception occurred while fetching user roles", HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public void assignRolesToUser(String id, AssignRolesRequest request) {
        log.info("Assigning roles to user with ID: {}", id);
        try {
            List<RoleRepresentation> roles = keycloak
                .realm(keycloakConfig.getRealm())
                .clients()
                .get(keycloakConfig.getClientId())
                .roles()
                .list()
                .stream()
                .filter(role -> request.roles().contains(role.getName()))
                .collect(Collectors.toList());
            log.info("Assigning {} roles to user with ID: {}", roles.size(), id);
            keycloak.realm(keycloakConfig.getRealm())
                .users()
                .get(id)
                .roles()
                .clientLevel(keycloakConfig.getClientUUID())
                .add(roles);
            log.info("Roles assigned successfully to user with ID: {}", id);
        } catch (Exception e) {
            log.error("Exception occurred while assigning roles: {}", e.getMessage());
            throw new TechnicalException("Exception occurred while assigning roles", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void removeRolesFromUser(String id, RemoveRolesRequest request) {
        log.info("Removing roles from user with ID: {}", id);
        try {
            List<RoleRepresentation> roles = keycloak
                .realm(keycloakConfig.getRealm())
                .clients()
                .get(keycloakConfig.getClientId())
                .roles()
                .list()
                .stream()
                .filter(role -> request.roles().contains(role.getName()))
                .collect(Collectors.toList());
            log.info("Removing {} roles from user with ID: {}", roles.size(), id);
            keycloak.realm(keycloakConfig.getRealm())
                .users()
                .get(id)
                .roles()
                .clientLevel(keycloakConfig.getClientUUID())
                .remove(roles);
            log.info("Roles removed successfully from user with ID: {}", id);
        } catch (Exception e) {
            log.error("Exception occurred while removing roles: {}", e.getMessage());
            throw new TechnicalException("Exception occurred while removing roles", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void resetUserPassword(String id, UpdatePasswordRequest request) {
        log.info("Resetting password for user with ID: {}", id);
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(request.newPassword());
        credential.setTemporary(false);

        try {
            keycloak
                .realm(keycloakConfig.getRealm())
                .users()
                .get(id)
                .resetPassword(credential);
            log.info("Password reset successfully for user with ID: {}", id);
        } catch (Exception e) {
            log.error("Exception occurred while resetting password: {}", e.getMessage());
            throw new TechnicalException("Exception occurred while resetting password", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
