package com.mizzle.blogrest.payload.request.blog;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
public class DeleteBoardRequest {

    @Schema( type = "long", example = "0", description="삭제할 개시글 ID 입니다.")
    @NotBlank
    private long boardId;

    public DeleteBoardRequest(){}

    @Builder
    public DeleteBoardRequest(long boardId){
        this.boardId = boardId;
    }
}
