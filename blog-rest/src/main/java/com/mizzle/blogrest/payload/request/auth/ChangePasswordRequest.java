package com.mizzle.blogrest.payload.request.auth;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @Schema( type = "string", example = "string")
    @NotBlank
    private String oldPassword;

    @Schema( type = "string", example = "string123")
    @NotBlank
    private String newPassword;

    @Schema( type = "string", example = "string123")
    @NotBlank
    private String reNewPassword;

}
