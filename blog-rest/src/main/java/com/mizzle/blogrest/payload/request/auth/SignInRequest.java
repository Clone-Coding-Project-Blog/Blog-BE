package com.mizzle.blogrest.payload.request.auth;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SignInRequest {

    @Schema( type = "string", example = "string@aa.bb")
    @NotBlank
    @Email
    private String email;

    @Schema( type = "string", example = "string")
    @NotBlank
    private String password;
}
