package com.mizzle.blogrest.payload.request.blog;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ReadBoardsRequest {
    
    @Schema( type = "string", example = "search", description="읽어올 개시글 목록들 중 제목이 동일한 값을 불러오기 위한 문자열 입니다.")
    private String search;

    @Schema( type = "int", example = "1", description="개시을 목록들 중 패이지 값입니다.")
    @NotBlank
    @Size(min = 0)
    private int page;

    @Schema( type = "int", example = "20", description="개시을 목록들 중 크기 값입니다.")
    @NotBlank
    @Size(min = 1)
    private int size;
}
