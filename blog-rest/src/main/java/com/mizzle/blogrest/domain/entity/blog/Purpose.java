package com.mizzle.blogrest.domain.entity.blog;

import lombok.Getter;

@Getter
public enum Purpose {
    finish("finish"),
    write("write");

    private final String value;

    Purpose(String value){
        this.value = value;
    }
}
