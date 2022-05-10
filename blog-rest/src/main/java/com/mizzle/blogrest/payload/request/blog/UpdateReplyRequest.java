package com.mizzle.blogrest.payload.request.blog;

import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Data;

@Data
public class UpdateReplyRequest {
        
    @NotBlank
    private long boardId;

    private long replyId;

    @NotBlank
    private String comment;

    public UpdateReplyRequest(){}

    @Builder
    public UpdateReplyRequest(long replyId, long boardId, String comment){
        this.replyId = replyId;
        this.boardId = boardId;
        this.comment = comment;
    }
}
