package org.boilerplate.technical.passport.payload.request;

import java.util.List;

public record RemoveRolesRequest(
    List<String> roles
) {
    
}
