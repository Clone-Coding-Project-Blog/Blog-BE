package com.mizzle.blogrest.payload.request.blog;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class DeleteBoardRequest {
    @NotBlank
    private long boardId;

}
