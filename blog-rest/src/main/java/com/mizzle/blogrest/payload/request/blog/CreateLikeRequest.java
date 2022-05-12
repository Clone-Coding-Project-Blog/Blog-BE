package com.mizzle.blogrest.payload.request.blog;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
public class CreateLikeRequest {

    @Schema( type = "long", example = "0", description="개시글의 ID 입니다.")
    @NotBlank
    private long boardId;

    public CreateLikeRequest(){}

    @Builder
    public CreateLikeRequest(long boardId){
        this.boardId = boardId;
    }
}
