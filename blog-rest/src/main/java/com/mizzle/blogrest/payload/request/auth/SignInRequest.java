package com.mizzle.blogrest.payload.request.auth;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class SignInRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
