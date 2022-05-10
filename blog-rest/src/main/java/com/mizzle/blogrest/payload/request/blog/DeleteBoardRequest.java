package com.mizzle.blogrest.payload.request.blog;

import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Data;

@Data
public class DeleteBoardRequest {
    @NotBlank
    private long boardId;

    public DeleteBoardRequest(){}

    @Builder
    public DeleteBoardRequest(long boardId){
        this.boardId = boardId;
    }
}
