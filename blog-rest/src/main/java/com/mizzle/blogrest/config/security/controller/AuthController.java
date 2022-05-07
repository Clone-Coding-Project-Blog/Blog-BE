package com.mizzle.blogrest.config.security.controller;


import javax.validation.Valid;

import com.mizzle.blogrest.config.security.service.AuthService;
import com.mizzle.blogrest.payload.request.SignInRequest;
import com.mizzle.blogrest.payload.request.SignUpRequest;
import com.mizzle.blogrest.payload.request.TokenRefreshRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/signin")
    public ResponseEntity<?> signin(@Valid @RequestBody SignInRequest signInRequest) {
        return authService.signin(signInRequest);
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        return authService.signup(signUpRequest);
    }

    @PostMapping(value = "/refresh")
    public ResponseEntity<?> refresh(@Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {
        return authService.refresh(tokenRefreshRequest);
    }

    @PostMapping(value="/signout")
    public ResponseEntity<?> postMethodName(@Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {
        return authService.signout(tokenRefreshRequest);
    }

}
