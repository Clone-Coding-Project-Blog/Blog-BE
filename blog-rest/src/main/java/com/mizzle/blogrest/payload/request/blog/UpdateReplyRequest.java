package com.mizzle.blogrest.payload.request.blog;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
public class UpdateReplyRequest {
    
    @Schema( type = "long", example = "0", description="댓글을 갱신하기 위해 개시글의 ID 입니다.")
    @NotBlank
    private long boardId;

    @Schema( type = "long", example = "0", description="댓글을 갱신하기 위해 선택된 댓글 ID 입니다.")
    @NotBlank
    private long replyId;

    @Schema( type = "string", example = "comment", description="댓글 내용 입니다.")
    @NotBlank
    private String comment;

    public UpdateReplyRequest(){}

    @Builder
    public UpdateReplyRequest(long replyId, long boardId, String comment){
        this.replyId = replyId;
        this.boardId = boardId;
        this.comment = comment;
    }
}
