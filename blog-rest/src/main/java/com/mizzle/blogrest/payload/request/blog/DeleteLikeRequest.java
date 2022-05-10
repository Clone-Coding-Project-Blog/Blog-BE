package com.mizzle.blogrest.payload.request.blog;

import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Data;

@Data
public class DeleteLikeRequest {
    @NotBlank
    private long boardId;

    public DeleteLikeRequest(){}

    @Builder
    public DeleteLikeRequest(long boardId){
        this.boardId = boardId;
    }
}
