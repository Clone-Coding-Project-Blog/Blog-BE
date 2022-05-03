package com.mizzle.blogrest.config.security.service;

import java.net.URI;
import java.util.Optional;

import com.mizzle.blogrest.advice.assertThat.DefaultAssert;
import com.mizzle.blogrest.config.security.token.UserPrincipal;
import com.mizzle.blogrest.domain.entity.user.Provider;
import com.mizzle.blogrest.domain.entity.user.Role;
import com.mizzle.blogrest.domain.entity.user.User;
import com.mizzle.blogrest.payload.Message;
import com.mizzle.blogrest.payload.request.LoginRequest;
import com.mizzle.blogrest.payload.request.SignUpRequest;
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

    public ResponseEntity<?> login(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = customTokenProviderService.createToken(authentication);
        return ResponseEntity.ok(new ApiResponse(true, new AuthResponse(token)) );
    }

    public ResponseEntity<?> create(SignUpRequest signUpRequest){
        DefaultAssert.isTrue(userRepository.existsByEmail(signUpRequest.getEmail()));
        
        User user = User.builder()
            .email(signUpRequest.getEmail())
            .name(signUpRequest.getName())
            .password(passwordEncoder.encode(signUpRequest.getPassword()))
            .provider(Provider.local)
            .role(Role.ADMIN)
            .build();

        userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/")
                .buildAndExpand(user.getId()).toUri();

        ApiResponse apiResponse = ApiResponse.builder().check(true).information(Message.builder().message(String.format("회원가입에 성공했습니다.")).build()).build();

        return ResponseEntity.created(location).body(apiResponse);
    }

    
    public ResponseEntity<?> delete(UserPrincipal userPrincipal){
        Optional<User> user = userRepository.findById(userPrincipal.getId());

        DefaultAssert.isOptionalPresent(user);
        String email = userPrincipal.getEmail();
        userRepository.delete(user.get());
        ApiResponse apiResponse = ApiResponse.builder().check(true).information(Message.builder().message(String.format("해당 %s 계정은 삭제 되었습니다.", email)).build()).build();
        return ResponseEntity.ok(apiResponse);
    }
}
