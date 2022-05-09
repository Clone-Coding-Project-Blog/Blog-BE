package com.mizzle.blogrest.payload.request.blog;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UpdateReplyRequest {
        
    @NotBlank
    private long boardId;

    @NotBlank
    private long replyId;

    @NotBlank
    private String comment;

}
