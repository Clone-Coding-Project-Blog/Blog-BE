package com.mizzle.blogrest.payload.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthResponse {

    private Object token;

    public AuthResponse(){};

    @Builder
    public AuthResponse(Object token) {
        this.token = token;
    }
}
