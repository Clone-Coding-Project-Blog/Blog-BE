package com.mizzle.blogrest.config.security.service;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import com.mizzle.blogrest.config.security.OAuth2Config;
import com.mizzle.blogrest.config.security.token.UserPrincipal;
import com.mizzle.blogrest.domain.mapping.TokenMapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomTokenProviderService {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";

    private Key key;
    private OAuth2Config oAuth2Config;

    public CustomTokenProviderService(OAuth2Config oAuth2Config) {
        this.oAuth2Config = oAuth2Config;
    }

    public TokenMapping createToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        String secretKey = oAuth2Config.getAuth().getTokenSecret();

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        key = Keys.hmacShaKeyFor(keyBytes);

        Date accessTokenExpiryDate = new Date(now.getTime() + oAuth2Config.getAuth().getAccessTokenExpirationMsec());
        Date refreshTokenExpiryDate = new Date(now.getTime() + oAuth2Config.getAuth().getRefreshTokenExpirationMsec());

        String accessToken = Jwts.builder()
                                .setSubject(Long.toString(userPrincipal.getId()))
                                .setIssuedAt(new Date())
                                .setExpiration(accessTokenExpiryDate)
                                .signWith(key, SignatureAlgorithm.HS512)
                                .compact();
        
        String refreshToken = Jwts.builder()
                                .setExpiration(refreshTokenExpiryDate)
                                .signWith(key, SignatureAlgorithm.HS512)
                                .compact();
        
        return TokenMapping.builder()
                            .grantType(BEARER_TYPE)
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .accessTokenExpiresIn(accessTokenExpiryDate.getTime())
                            .build();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(oAuth2Config.getAuth().getTokenSecret())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);
        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }
        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities = Arrays
                .stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Long getExpiration(String accessToken) {
        // accessToken 남은 유효시간
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody().getExpiration();
        // 현재 시간
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(oAuth2Config.getAuth().getTokenSecret()).build().parseClaimsJws(authToken);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException ex) {
            log.error("잘못된 JWT 서명입니다.");
        } catch (MalformedJwtException ex) {
            log.error("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException ex) {
            log.error("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException ex) {
            log.error("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException ex) {
            log.error("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}
