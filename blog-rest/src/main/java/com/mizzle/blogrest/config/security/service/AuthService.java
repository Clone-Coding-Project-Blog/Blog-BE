package com.mizzle.blogrest.config.security.service;

import java.net.URI;
import java.util.Optional;

import javax.transaction.Transactional;

import com.mizzle.blogrest.advice.assertThat.DefaultAssert;
import com.mizzle.blogrest.config.security.repository.TokenRepository;
import com.mizzle.blogrest.config.security.token.UserPrincipal;
import com.mizzle.blogrest.domain.entity.user.Provider;
import com.mizzle.blogrest.domain.entity.user.Role;
import com.mizzle.blogrest.domain.entity.user.Token;
import com.mizzle.blogrest.domain.entity.user.User;
import com.mizzle.blogrest.domain.mapping.TokenMapping;
import com.mizzle.blogrest.payload.Message;
import com.mizzle.blogrest.payload.request.LoginRequest;
import com.mizzle.blogrest.payload.request.SignUpRequest;
import com.mizzle.blogrest.payload.request.TokenRefreshRequest;
import com.mizzle.blogrest.payload.request.TokenValidRequest;
import com.mizzle.blogrest.payload.response.ApiResponse;
import com.mizzle.blogrest.payload.response.AuthResponse;
import com.mizzle.blogrest.repository.user.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomTokenProviderService customTokenProviderService;
    private final TokenRepository tokenRepository;

    public ResponseEntity<?> login(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenMapping token = customTokenProviderService.createToken(authentication);
        
        Token refreshToken = Token.builder()
                .key(authentication.getName())
                .refresh(token.getRefreshToken())
                .access(token.getAccessToken())
                .build();

        tokenRepository.save(refreshToken);

        return ResponseEntity.ok(new ApiResponse(true, new AuthResponse(token)) );
    }

    public ResponseEntity<?> create(SignUpRequest signUpRequest){
        log.info("create={}",signUpRequest.getEmail());
        //DefaultAssert.isTrue(userRepository.existsByEmail(signUpRequest.getEmail()));
        
        User user = User.builder()
            .email(signUpRequest.getEmail())
            .name(signUpRequest.getName())
            .password(passwordEncoder.encode(signUpRequest.getPassword()))
            .provider(Provider.local)
            .role(Role.ADMIN)
            .build();

        userRepository.save(user);

        
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    signUpRequest.getEmail(),
                    signUpRequest.getPassword()
                )
        );

        TokenMapping tokenMapping = customTokenProviderService.createToken(authentication);

        Token token = Token.builder()
                .key(authentication.getName())
                .refresh(tokenMapping.getRefreshToken())
                .access(tokenMapping.getAccessToken())
                .build();

        tokenRepository.save(token);
        
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/")
                .buildAndExpand(user.getId()).toUri();


        ApiResponse apiResponse = ApiResponse.builder().check(true).information(Message.builder().message(String.format("회원가입에 성공했습니다.")).build()).build();
        return ResponseEntity.created(location).body(apiResponse);
    }
    
    @Transactional
    public ResponseEntity<?> valid(TokenValidRequest tokenValidRequest) {

        // 1. Refresh Token 검증
        if (!customTokenProviderService.validateToken(tokenValidRequest.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }
        
        Authentication authentication = customTokenProviderService.getAuthentication(tokenValidRequest.getAccessToken());

        if(!authentication.isAuthenticated()){
            throw new RuntimeException("authentication 인증이 올바르지 않습니다.");
        };

        Optional<Token> refreshToken = tokenRepository.findByKey(authentication.getName());

        if(refreshToken.isPresent()){
            new RuntimeException("Refresh Token 이 데이터베이스에 존재하지 않습니다.");
        }

        if(!refreshToken.get().getRefresh().equals(tokenValidRequest.getRefreshToken())){
            throw new RuntimeException("Refresh Token 정보가 일치하지 않습니다.");
        }

        ApiResponse apiResponse = ApiResponse.builder().check(true).information(tokenValidRequest).build();
        return ResponseEntity.ok(apiResponse);
    }

    
    @Transactional
    public ResponseEntity<?> refresh(TokenRefreshRequest tokenRefreshRequest) {

        // 1. refresh token 확인
        if (!customTokenProviderService.validateToken(tokenRefreshRequest.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 3. token 검색
        Token token = tokenRepository.findByRefresh(tokenRefreshRequest.getRefreshToken()).orElseThrow(
            () -> new RuntimeException("Refresh Token이 데이터베이스에 존재하지 않습니다.")
        );

        // 4. Access Token 에서 Member ID 가져오기
        Authentication authentication = customTokenProviderService.getAuthentication(token.getAccess());

        if(!authentication.isAuthenticated()){
            throw new RuntimeException("authentication 인증이 올바르지 않습니다.");
        };

        // 5. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        Token refreshToken = tokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 6. Refresh Token 일치하는지 검사
        if (!refreshToken.getRefresh().equals(tokenRefreshRequest.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 7. 새로운 토큰 생성
        TokenMapping tokenMapping = customTokenProviderService.createToken(authentication);

        // 8. 저장소 정보 업데이트
        Token newRefreshToken = refreshToken.updateValue(tokenMapping.getRefreshToken());
        tokenRepository.save(newRefreshToken);

        // 토큰 발급
        ApiResponse apiResponse = ApiResponse.builder().check(true).information(newRefreshToken).build();
        return ResponseEntity.ok(apiResponse);
    }

}
