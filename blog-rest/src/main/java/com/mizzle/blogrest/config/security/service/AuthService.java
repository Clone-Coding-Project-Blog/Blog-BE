package com.mizzle.blogrest.config.security.service;

import java.net.URI;

import com.mizzle.blogrest.entity.user.Provider;
import com.mizzle.blogrest.entity.user.Role;
import com.mizzle.blogrest.entity.user.User;
import com.mizzle.blogrest.error.BadRequestException;
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
        return ResponseEntity.ok(new AuthResponse(token));
    }

    public ResponseEntity<?> create(SignUpRequest signUpRequest){
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }

        User user = new User();
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());
        user.setProvider(Provider.local);
        user.setRole(Role.ADMIN);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully!"));
    }

}
