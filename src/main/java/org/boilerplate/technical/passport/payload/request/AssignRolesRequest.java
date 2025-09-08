package org.boilerplate.technical.passport.payload.request;

import java.util.List;

public record AssignRolesRequest(
    List<String> roles
) {
    
}
