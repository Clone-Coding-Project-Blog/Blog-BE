package com.mizzle.blogrest.payload.request.blog;

import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Data;

@Data
public class ReadReplysRequest {
    @NotBlank
    private long boardId;

    ReadReplysRequest(){};

    @Builder
    public ReadReplysRequest(long boardId){
        this.boardId = boardId;
    };
}
