package com.mizzle.blogrest.payload.request.blog;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
public class ReadReplysRequest {
    
    @Schema( type = "long", example = "0", description="개시을 목록들 중 크기 값입니다.")
    @NotBlank
    private long boardId;

    ReadReplysRequest(){};

    @Builder
    public ReadReplysRequest(long boardId){
        this.boardId = boardId;
    };
}
