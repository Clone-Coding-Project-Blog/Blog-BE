package com.mizzle.blogrest.payload.request;

import lombok.Builder;
import lombok.Data;

@Data
public class TokenRefreshRequest {
    private String refreshToken;

    public TokenRefreshRequest(){}

    @Builder
    public TokenRefreshRequest(String refreshToken){
        this.refreshToken = refreshToken;
    }
}
