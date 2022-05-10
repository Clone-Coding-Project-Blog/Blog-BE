package com.mizzle.blogrest.payload.response.blog;

import lombok.Builder;
import lombok.Data;

@Data
public class ReadLikeResponse {
    private boolean likeCheck;
    private long count;

    @Builder
    public ReadLikeResponse(boolean likeCheck, long count){
        this.likeCheck = likeCheck;
        this.count = count;
    }
}
