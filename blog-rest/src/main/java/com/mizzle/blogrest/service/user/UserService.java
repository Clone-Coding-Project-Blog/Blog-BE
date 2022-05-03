package com.mizzle.blogrest.service.user;

import java.util.Optional;

import com.mizzle.blogrest.advice.assertThat.DefaultAssert;
import com.mizzle.blogrest.config.security.token.UserPrincipal;
import com.mizzle.blogrest.domain.entity.user.User;
import com.mizzle.blogrest.payload.Message;
import com.mizzle.blogrest.payload.response.ApiResponse;
import com.mizzle.blogrest.repository.user.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public ResponseEntity<?> readByUser(UserPrincipal userPrincipal){
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isOptionalPresent(user);
        ApiResponse apiResponse = ApiResponse.builder().check(true).information(user.get()).build();
        return ResponseEntity.ok(apiResponse);
    }


}
