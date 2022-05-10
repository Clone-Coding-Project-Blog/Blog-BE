package com.mizzle.blogrest.payload.request.auth;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Builder;
import lombok.Data;

@Data
public class RefreshTokenRequest {
    
    @Schema( type = "string", example = "eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2NTI3OTgxOTh9.6CoxHB_siOuz6PxsxHYQCgUT1_QbdyKTUwStQDutEd1-cIIARbQ0cyrnAmpIgi3IBoLRaqK7N1vXO42nYy4g5g")
    @NotBlank
    private String refreshToken;

    public RefreshTokenRequest(){}

    @Builder
    public RefreshTokenRequest(String refreshToken){
        this.refreshToken = refreshToken;
    }
}