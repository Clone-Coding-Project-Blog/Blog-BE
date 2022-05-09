package com.mizzle.blogrest.payload.request.auth;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ChangePasswordRequest {

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;

    @NotBlank
    private String reNewPassword;

}
