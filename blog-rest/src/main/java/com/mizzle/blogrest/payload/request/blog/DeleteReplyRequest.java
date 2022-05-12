package com.mizzle.blogrest.payload.request.blog;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DeleteReplyRequest {
    
    @Schema( type = "long", example = "0", description="댓글을 삭제하기 위해 선택된 개시글 ID 입니다.")
    @NotBlank
    private long boardId;

    @Schema( type = "reply", example = "0", description="댓글을 삭제하기 위해 선택된 댓글 ID 입니다.")
    @NotBlank
    private long replyId;
}
