package com.mizzle.blogrest.payload.request.blog;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
public class DeleteLikeRequest {

    @Schema( type = "long", example = "0", description="좋아요를 제거하기 위해 선택된 개시글 ID 입니다.")
    @NotBlank
    private long boardId;

    public DeleteLikeRequest(){}

    @Builder
    public DeleteLikeRequest(long boardId){
        this.boardId = boardId;
    }
}
