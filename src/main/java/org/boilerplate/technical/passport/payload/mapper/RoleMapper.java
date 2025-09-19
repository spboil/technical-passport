package org.boilerplate.technical.passport.payload.mapper;

import org.boilerplate.technical.passport.payload.request.RoleRequest;
import org.boilerplate.technical.passport.payload.response.RoleResponse;
import org.keycloak.representations.idm.RoleRepresentation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleMapper {
    
    public static RoleRepresentation toRoleRepresentation(RoleRequest request) {
        RoleRepresentation role =  new RoleRepresentation();
        role.setName(request.name());
        role.setDescription(request.description());
        return role;
    }

    public static RoleResponse toRoleResponse(RoleRepresentation roleRepresentation) {
        return new RoleResponse(
            roleRepresentation.getId(),
            roleRepresentation.getName(),
            roleRepresentation.getDescription()
        );
    }

}
