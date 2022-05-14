package com.mizzle.blogrest.payload.request.blog;


import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateReplyRequest {

    @Schema( type = "string", example = "comment", description="댓글 내용 입니다.")
    @NotBlank
    private String comment;

}
