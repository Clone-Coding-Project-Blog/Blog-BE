package com.mizzle.blogrest.config.security.controller;


import javax.validation.Valid;

import com.mizzle.blogrest.advice.payload.ErrorResponse;
import com.mizzle.blogrest.config.security.service.AuthService;
import com.mizzle.blogrest.config.security.token.CurrentUser;
import com.mizzle.blogrest.config.security.token.UserPrincipal;
import com.mizzle.blogrest.domain.entity.user.User;
import com.mizzle.blogrest.payload.Message;
import com.mizzle.blogrest.payload.request.auth.ChangePasswordRequest;
import com.mizzle.blogrest.payload.request.auth.SignInRequest;
import com.mizzle.blogrest.payload.request.auth.SignUpRequest;
import com.mizzle.blogrest.payload.response.AuthResponse;
import com.mizzle.blogrest.payload.request.auth.RefreshTokenRequest;

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
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import lombok.RequiredArgsConstructor;

@Tag(name = "Authorization", description = "Authorization API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "유저 정보 확인", description = "현제 접속된 유저정보를 확인합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "유저 확인 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class) ) } ),
        @ApiResponse(responseCode = "400", description = "유저 확인 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping(value = "/")
    public ResponseEntity<?> whoAmI(@CurrentUser UserPrincipal userPrincipal) {
        return authService.whoAmI(userPrincipal);
    }

    @Operation(summary = "유저 정보 삭제", description = "현제 접속된 유저정보를 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "유저 삭제 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class) ) } ),
        @ApiResponse(responseCode = "400", description = "유저 삭제 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @DeleteMapping(value = "/")
    public ResponseEntity<?> delete(@CurrentUser UserPrincipal userPrincipal){
        return authService.delete(userPrincipal);
    }

    @Operation(summary = "유저 정보 갱신", description = "현제 접속된 유저의 비밀번호를 새로 지정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "유저 정보 갱신 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class) ) } ),
        @ApiResponse(responseCode = "400", description = "유저 정보 갱신 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PutMapping(value = "/")
    public ResponseEntity<?> modify(@CurrentUser UserPrincipal userPrincipal, @Valid @RequestBody ChangePasswordRequest passwordChangeRequest){
        return authService.modify(userPrincipal, passwordChangeRequest);
    }

    @Operation(summary = "유저 로그인", description = "유저 로그인을 수행합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "유저 로그인 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class) ) } ),
        @ApiResponse(responseCode = "400", description = "유저 로그인 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping(value = "/signin")
    public ResponseEntity<?> signin(@Valid @RequestBody SignInRequest signInRequest) {
        return authService.signin(signInRequest);
    }

    @Operation(summary = "유저 회원가입", description = "유저 회원가입을 수행합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원가입 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class) ) } ),
        @ApiResponse(responseCode = "400", description = "회원가입 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping(value = "/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        return authService.signup(signUpRequest);
    }

    @Operation(summary = "토큰 갱신", description = "신규 토큰 갱신을 수행합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "토큰 갱신 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class) ) } ),
        @ApiResponse(responseCode = "400", description = "토큰 갱신 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping(value = "/refresh")
    public ResponseEntity<?> refresh(@Valid @RequestBody RefreshTokenRequest tokenRefreshRequest) {
        return authService.refresh(tokenRefreshRequest);
    }


    @Operation(summary = "유저 로그아웃", description = "유저 로그아웃을 수행합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그아웃 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class) ) } ),
        @ApiResponse(responseCode = "400", description = "로그아웃 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping(value="/signout")
    public ResponseEntity<?> signout(@Valid @RequestBody RefreshTokenRequest tokenRefreshRequest) {
        return authService.signout(tokenRefreshRequest);
    }

}
