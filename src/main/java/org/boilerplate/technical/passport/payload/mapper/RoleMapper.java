package org.boilerplate.technical.passport.payload.mapper;

import org.boilerplate.technical.passport.payload.request.RoleRequest;
import org.boilerplate.technical.passport.payload.response.RoleResponse;
import org.keycloak.representations.idm.RoleRepresentation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleMapper {
    
    public static RoleRepresentation toRoleRepresentation(RoleRequest response) {
        return null;
    }

    public static RoleResponse toRoleResponse(RoleRepresentation roleRepresentation) {
        return null;
    }

}
