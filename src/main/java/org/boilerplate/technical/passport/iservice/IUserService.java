package org.boilerplate.technical.passport.iservice;

import java.util.List;

import org.boilerplate.technical.passport.payload.request.AssignRolesRequest;
import org.boilerplate.technical.passport.payload.request.RemoveRolesRequest;
import org.boilerplate.technical.passport.payload.request.UpdatePasswordRequest;
import org.boilerplate.technical.passport.payload.request.UserCreateRequest;
import org.boilerplate.technical.passport.payload.request.UserUpdateRequest;
import org.boilerplate.technical.passport.payload.response.RoleResponse;
import org.boilerplate.technical.passport.payload.response.UserResponse;

public interface IUserService {
    
    UserResponse createUser(UserCreateRequest request);
    UserResponse updateUser(String id, UserUpdateRequest request);
    void deleteUser(String id);

    UserResponse getUserById(String id);

    List<UserResponse> getAllUsers(int page, int size);

    List<RoleResponse> getUserRoles(String id);

    void assignRolesToUser(String id, AssignRolesRequest request);
    void removeRolesFromUser(String id, RemoveRolesRequest request);

    void resetUserPassword(String id, UpdatePasswordRequest request);
}
