package com.mizzle.blogrest.payload.request.blog;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.mizzle.blogrest.domain.entity.blog.Purpose;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
public class UpdateBoardRequest {
    
    @Schema( type = "long", example = "0", description="개시글 ID 입니다.")
    @NotBlank(message = "제목은 필수 입력 값입니다.")
    @Size(min = 1, max = 128)
    private long boardId;

    @Schema( type = "string", example = "title", description="개시글 제목 입니다.")
    @NotBlank(message = "부 제목은 필수 입력 값입니다.")
    @Size(min = 1, max = 256)
    private String title;

    @Schema( type = "string", example = "subtitle", description="개시글 부제목 입니다.")
    @NotBlank(message = "Markdown은 필수 입력 값입니다.")
    private String subtitle;

    @Schema( type = "string", example = "markdown", description="개시글 Markdown 입니다.")
    @NotBlank(message = "Markdown은 필수 입력 값입니다.")
    private String markdown;

    @Schema( type = "String", example = "html", description="개시글 Html 입니다.")
    @NotBlank(message = "Html은 필수 입력 값입니다.")
    private String html;

    @Schema( type = "List", example = "[\"tag1\",\"tag2\"]", description="개시글의 Tag(들) 입니다." )
    @NotBlank(message = "Tag는 필수 입력 값입니다.")
    private List<String> tagNames;

    @Schema( type = "string", example = "finish" , description="작성 상태(write,finish) 입니다." )
    @NotBlank(message = "작성 상태는 필수 입력 값입니다.")
    private Purpose purpose;

    public UpdateBoardRequest(){}

    @Builder
    public UpdateBoardRequest(long boardId, String title, String subtitle, String markdown, String html, List<String> tagNames, Purpose purpose){
        this.boardId = boardId;
        this.title = title;
        this.subtitle = subtitle;
        this.markdown = markdown;
        this.html = html;
        this.tagNames = tagNames;
        this.purpose = purpose;
    }
}
