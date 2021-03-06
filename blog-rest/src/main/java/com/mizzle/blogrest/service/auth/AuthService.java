package com.mizzle.blogrest.service.auth;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import com.mizzle.blogrest.advice.assertThat.DefaultAssert;
import com.mizzle.blogrest.config.security.token.UserPrincipal;
import com.mizzle.blogrest.domain.entity.blog.Board;
import com.mizzle.blogrest.domain.entity.user.Provider;
import com.mizzle.blogrest.domain.entity.user.Role;
import com.mizzle.blogrest.domain.entity.user.Token;
import com.mizzle.blogrest.domain.entity.user.User;
import com.mizzle.blogrest.domain.mapping.TokenMapping;
import com.mizzle.blogrest.payload.request.auth.ChangePasswordRequest;
import com.mizzle.blogrest.payload.request.auth.SignInRequest;
import com.mizzle.blogrest.payload.request.auth.SignUpRequest;
import com.mizzle.blogrest.payload.request.auth.RefreshTokenRequest;
import com.mizzle.blogrest.payload.response.ApiResponse;
import com.mizzle.blogrest.payload.response.AuthResponse;
import com.mizzle.blogrest.payload.response.Message;
import com.mizzle.blogrest.repository.auth.TokenRepository;
import com.mizzle.blogrest.repository.blog.BoardRepository;
import com.mizzle.blogrest.repository.blog.LikeRepository;
import com.mizzle.blogrest.repository.blog.ReplyRepository;
import com.mizzle.blogrest.repository.blog.TagRepository;
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
    private final PasswordEncoder passwordEncoder;
    private final CustomTokenProviderService customTokenProviderService;
    
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final BoardRepository boardRepository;
    private final LikeRepository likeRepository;
    private final TagRepository tagRepository;
    private final ReplyRepository replyRepository;
    

    public ResponseEntity<?> whoAmI(UserPrincipal userPrincipal){
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isOptionalPresent(user);
        ApiResponse apiResponse = ApiResponse.builder().check(true).information(user.get()).build();

        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> delete(UserPrincipal userPrincipal){
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "????????? ???????????? ????????????.");

        Optional<Token> token = tokenRepository.findByUserEmail(user.get().getEmail());
        DefaultAssert.isTrue(token.isPresent(), "????????? ???????????? ????????????.");

        likeRepository.deleteAllByUser(user.get());
        replyRepository.deleteAllByUserId(user.get().getId());

        List<Board> boards = boardRepository.findByUser(user.get());
        for(Board board: boards){
            tagRepository.deleteAllByBoardId(board.getId());
        }

        boardRepository.deleteAllByUser(user.get());

        userRepository.delete(user.get());
        tokenRepository.delete(token.get());

        ApiResponse apiResponse = ApiResponse.builder().check(true).information(Message.builder().message("?????? ?????????????????????.").build()).build();

        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> modify(UserPrincipal userPrincipal, ChangePasswordRequest passwordChangeRequest){
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        boolean passwordCheck = passwordEncoder.matches(passwordChangeRequest.getOldPassword(),user.get().getPassword());
        DefaultAssert.isTrue(passwordCheck, "????????? ???????????? ?????????.");

        boolean newPasswordCheck = passwordChangeRequest.getNewPassword().equals(passwordChangeRequest.getReNewPassword());
        DefaultAssert.isTrue(newPasswordCheck, "?????? ?????? ???????????? ?????? ???????????? ????????????.");


        passwordEncoder.encode(passwordChangeRequest.getNewPassword());

        return ResponseEntity.ok(true);
    }

    public ResponseEntity<?> signin(SignInRequest signInRequest){
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                signInRequest.getEmail(),
                signInRequest.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenMapping tokenMapping = customTokenProviderService.createToken(authentication);
        Token token = Token.builder()
                            .refreshToken(tokenMapping.getRefreshToken())
                            .userEmail(tokenMapping.getUserEmail())
                            .build();
        tokenRepository.save(token);
        AuthResponse authResponse = AuthResponse.builder().accessToken(tokenMapping.getAccessToken()).refreshToken(token.getRefreshToken()).build();
        
        return ResponseEntity.ok(authResponse);
    }

    public ResponseEntity<?> signup(SignUpRequest signUpRequest){
        DefaultAssert.isTrue(!userRepository.existsByEmail(signUpRequest.getEmail()), "?????? ???????????? ???????????? ????????????.");

        User user = User.builder()
                        .name(signUpRequest.getName())
                        .email(signUpRequest.getEmail())
                        .password(passwordEncoder.encode(signUpRequest.getPassword()))
                        .provider(Provider.local)
                        .role(Role.ADMIN)
                        .build();

        userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/auth/")
                .buildAndExpand(user.getId()).toUri();
        ApiResponse apiResponse = ApiResponse.builder().check(true).information(Message.builder().message("??????????????? ?????????????????????.").build()).build();

        return ResponseEntity.created(location).body(apiResponse);
    }

    public ResponseEntity<?> refresh(RefreshTokenRequest tokenRefreshRequest){
        //1??? ??????
        boolean checkValid = valid(tokenRefreshRequest.getRefreshToken());
        DefaultAssert.isAuthentication(checkValid);

        Optional<Token> token = tokenRepository.findByRefreshToken(tokenRefreshRequest.getRefreshToken());
        Authentication authentication = customTokenProviderService.getAuthenticationByEmail(token.get().getUserEmail());

        //4. refresh token ?????? ?????? ???????????? ??????.
        //?????? ????????? ??????
        TokenMapping tokenMapping;

        Long expirationTime = customTokenProviderService.getExpiration(tokenRefreshRequest.getRefreshToken());
        if(expirationTime > 0){
            tokenMapping = customTokenProviderService.refreshToken(authentication, token.get().getRefreshToken());
        }else{
            tokenMapping = customTokenProviderService.createToken(authentication);
        }

        Token updateToken = token.get().updateRefreshToken(tokenMapping.getRefreshToken());
        tokenRepository.save(updateToken);

        AuthResponse authResponse = AuthResponse.builder().accessToken(tokenMapping.getAccessToken()).refreshToken(updateToken.getRefreshToken()).build();

        return ResponseEntity.ok(authResponse);
    }

    public ResponseEntity<?> signout(RefreshTokenRequest tokenRefreshRequest){
        boolean checkValid = valid(tokenRefreshRequest.getRefreshToken());
        DefaultAssert.isAuthentication(checkValid);

        //4 token ????????? ????????????.
        Optional<Token> token = tokenRepository.findByRefreshToken(tokenRefreshRequest.getRefreshToken());
        tokenRepository.delete(token.get());
        ApiResponse apiResponse = ApiResponse.builder().check(true).information(Message.builder().message("???????????? ???????????????.").build()).build();

        return ResponseEntity.ok(apiResponse);
    }

    private boolean valid(String refreshToken){

        //1. ?????? ?????? ????????? ??????
        boolean validateCheck = customTokenProviderService.validateToken(refreshToken);
        DefaultAssert.isTrue(validateCheck, "Token ????????? ?????????????????????.");

        //2. refresh token ?????? ????????????.
        Optional<Token> token = tokenRepository.findByRefreshToken(refreshToken);
        DefaultAssert.isTrue(token.isPresent(), "?????? ????????? ???????????????.");

        //3. email ?????? ?????? ???????????? ????????????
        Authentication authentication = customTokenProviderService.getAuthenticationByEmail(token.get().getUserEmail());
        DefaultAssert.isTrue(token.get().getUserEmail().equals(authentication.getName()), "????????? ????????? ?????????????????????.");

        return true;
    }


}
