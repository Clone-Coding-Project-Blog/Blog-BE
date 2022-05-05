package com.mizzle.blogrest.domain.entity.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;

@Getter
@Table(name="token")
@Entity
public class Token {

    @Id
    @Column(name = "token_id", length = 1024 , nullable = false)
    private String key;

    @Column(name = "refresh_token", length = 1024 , nullable = false)
    private String refresh;

    @Column(name = "access_token", length = 1024 , nullable = false)
    private String access;

    public Token(){}

    public Token updateValue(String refresh) {
        this.refresh = refresh;
        return this;
    }

    @Builder
    public Token(String key, String access, String refresh) {
        this.key = key;
        this.access = access;
        this.refresh = refresh;
    }
}
