package org.boilerplate.technical.passport.payload.mapper;

import org.boilerplate.technical.passport.payload.request.UserCreateRequest;
import org.boilerplate.technical.passport.payload.request.UserUpdateRequest;
import org.boilerplate.technical.passport.payload.response.UserResponse;
import org.keycloak.representations.idm.UserRepresentation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    
    public static UserRepresentation toUserRepresentation(UserCreateRequest request){
        UserRepresentation user = new UserRepresentation();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setUsername(request.username());
        user.setEnabled(true);
        return user;
    }

    public static UserRepresentation toUserRepresentation(UserUpdateRequest request){
        UserRepresentation user = new UserRepresentation();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        return user;
    }

    public static UserResponse fromUserRepresentation(UserRepresentation user){
        return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName()
        );
    }

}
