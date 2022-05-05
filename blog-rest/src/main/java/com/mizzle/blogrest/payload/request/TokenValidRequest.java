package com.mizzle.blogrest.payload.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TokenValidRequest {
    private String accessToken;
    private String refreshToken;
    
    @Builder
    public TokenValidRequest(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
