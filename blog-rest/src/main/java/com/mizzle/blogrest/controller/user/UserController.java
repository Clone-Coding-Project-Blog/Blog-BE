package com.mizzle.blogrest.controller.user;

import com.mizzle.blogrest.advice.payload.ErrorResponse;
import com.mizzle.blogrest.config.security.token.CurrentUser;
import com.mizzle.blogrest.config.security.token.UserPrincipal;
import com.mizzle.blogrest.entity.user.User;
import com.mizzle.blogrest.error.BadRequestException;
import com.mizzle.blogrest.repository.user.UserRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserRepository userRepository;

    @Operation(summary = "정보입력", description = "시뮬레이션을 위한 데이터셋 위치 및 정보를 입력합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저장 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserPrincipal.class) ) } ),
            @ApiResponse(responseCode = "400", description = "저장 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping("/user/me")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new BadRequestException(""+userPrincipal.getId()));
    }
}
