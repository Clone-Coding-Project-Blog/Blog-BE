package com.mizzle.blogrest.payload.request.blog;


import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class CreateReplyRequest {
    
    @NotBlank
    private long boardId;

    @NotBlank
    private String comment;

}
