package com.mizzle.blogrest.payload.response.blog;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
public class ReadLikeResponse {

    @Schema( type = "boolean", example = "likeCheck", description="좋아요 채크 여부 입니다.")
    @NotBlank
    private boolean likeCheck;

    @Schema( type = "string", example = "comment", description="댓글 내용 입니다.")
    @NotBlank
    private long count;

    @Builder
    public ReadLikeResponse(boolean likeCheck, long count){
        this.likeCheck = likeCheck;
        this.count = count;
    }
}
