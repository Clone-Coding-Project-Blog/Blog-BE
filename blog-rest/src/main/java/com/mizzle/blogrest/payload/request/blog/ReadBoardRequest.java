package com.mizzle.blogrest.payload.request.blog;

import lombok.Builder;
import lombok.Data;

@Data
public class ReadBoardRequest {
    private long boardId;

    ReadBoardRequest(){}

    @Builder
    public ReadBoardRequest(long boardId){
        this.boardId = boardId;
    }
}
