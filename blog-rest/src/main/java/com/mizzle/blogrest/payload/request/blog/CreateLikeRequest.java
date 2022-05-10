package com.mizzle.blogrest.payload.request.blog;

import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Data;

@Data
public class CreateLikeRequest {
    @NotBlank
    private long boardId;

    public CreateLikeRequest(){}

    @Builder
    public CreateLikeRequest(long boardId){
        this.boardId = boardId;
    }
}
