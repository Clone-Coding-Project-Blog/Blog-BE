package com.mizzle.blogrest.payload.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TokenRefreshRequest {
    private String refreshToken;
}
