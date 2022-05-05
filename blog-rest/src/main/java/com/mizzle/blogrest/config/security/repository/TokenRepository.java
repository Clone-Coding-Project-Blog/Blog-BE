package com.mizzle.blogrest.config.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import com.mizzle.blogrest.domain.entity.user.Token;


@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByKey(String key);
    Optional<Token> findByAccess(String access);
    Optional<Token> findByRefresh(String refresh);
    
}