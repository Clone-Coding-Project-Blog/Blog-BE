package com.mizzle.blogrest.payload.request.blog;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
public class ReadBoardRequest {

    @Schema( type = "long", example = "0", description="읽어올 개시글 ID 입니다.")
    private long boardId;

    ReadBoardRequest(){}

    @Builder
    public ReadBoardRequest(long boardId){
        this.boardId = boardId;
    }
}
