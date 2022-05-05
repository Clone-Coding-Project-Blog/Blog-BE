package com.mizzle.blogrest.config.security.controller;


import javax.validation.Valid;

import com.mizzle.blogrest.advice.assertThat.DefaultAssert;
import com.mizzle.blogrest.config.security.service.AuthService;
import com.mizzle.blogrest.config.security.token.CurrentUser;
import com.mizzle.blogrest.config.security.token.UserPrincipal;
import com.mizzle.blogrest.payload.request.LoginRequest;
import com.mizzle.blogrest.payload.request.SignUpRequest;
import com.mizzle.blogrest.payload.request.TokenRefreshRequest;
import com.mizzle.blogrest.payload.request.TokenValidRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> create(@Valid @RequestBody SignUpRequest signUpRequest) {
        return authService.create(signUpRequest);
    }

    @PostMapping(value = "/valid")
    public ResponseEntity<?> valid(@RequestBody TokenValidRequest tokenValidRequest) {
        return authService.valid(tokenValidRequest);
    }

    @PostMapping(value = "/refresh")
    public ResponseEntity<?> refresh(@RequestBody TokenRefreshRequest tokenRefreshRequest) {
        return authService.refresh(tokenRefreshRequest);
    }

}
