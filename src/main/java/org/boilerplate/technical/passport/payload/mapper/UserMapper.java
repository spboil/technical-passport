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
        return null;
    }

    public static UserRepresentation toUserRepresentation(UserUpdateRequest request){
        return null;
    }

    public static UserResponse fromUserRepresentation(UserRepresentation user){
        return null;
    }

}
