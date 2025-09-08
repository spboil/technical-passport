package org.boilerplate.technical.passport.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserCreateRequest(

    @NotBlank(message = "Username is mandatory")
    String username,

    @NotBlank(message = "Firstname is mandatory")
    String firstName,

    @NotBlank(message = "Lastname is mandatory")
    String lastName,

    @Email(message = "Email should be valid")
    String email
) {
    
}
