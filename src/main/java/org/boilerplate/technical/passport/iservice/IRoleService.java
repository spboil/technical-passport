package org.boilerplate.technical.passport.iservice;

import java.util.List;

import org.boilerplate.technical.passport.payload.request.RoleRequest;
import org.boilerplate.technical.passport.payload.response.RoleResponse;

public interface IRoleService {
    
    RoleResponse createRole(RoleRequest request);
    void deleteRole(String name);

    RoleResponse getRole(String name);

    List<RoleResponse> getAllRoles(int page, int size);
}
