package com.mizzle.blogrest.config.security.controller;


import javax.validation.Valid;

import com.mizzle.blogrest.advice.payload.ErrorResponse;
import com.mizzle.blogrest.config.security.service.AuthService;
import com.mizzle.blogrest.config.security.token.CurrentUser;
import com.mizzle.blogrest.config.security.token.UserPrincipal;
import com.mizzle.blogrest.payload.request.auth.PasswordChangeRequest;
import com.mizzle.blogrest.payload.request.auth.SignInRequest;
import com.mizzle.blogrest.payload.request.auth.SignUpRequest;
import com.mizzle.blogrest.payload.request.auth.TokenRefreshRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "유저 정보 확인", description = "현제 접속된 유저정보를 확인합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "유저 확인 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserPrincipal.class) ) } ),
        @ApiResponse(responseCode = "400", description = "유저 확인 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping(value = "/")
    public ResponseEntity<?> whoAmI(@CurrentUser UserPrincipal userPrincipal) {
        return authService.whoAmI(userPrincipal);
    }

    @DeleteMapping(value = "/")
    public ResponseEntity<?> delete(@CurrentUser UserPrincipal userPrincipal){
        return authService.delete(userPrincipal);
    }

    @PutMapping(value = "/")
    public ResponseEntity<?> modify(@CurrentUser UserPrincipal userPrincipal, @Valid @RequestBody PasswordChangeRequest passwordChangeRequest){
        return authService.modify(userPrincipal, passwordChangeRequest);
    }

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
