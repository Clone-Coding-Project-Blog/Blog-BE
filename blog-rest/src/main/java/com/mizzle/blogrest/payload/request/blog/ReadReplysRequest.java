package com.mizzle.blogrest.payload.request.blog;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ReadReplysRequest {
    @NotBlank
    private long boardId;
}
