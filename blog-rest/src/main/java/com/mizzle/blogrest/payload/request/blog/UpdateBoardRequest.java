package com.mizzle.blogrest.payload.request.blog;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.mizzle.blogrest.domain.entity.blog.Purpose;

import lombok.Builder;
import lombok.Data;

@Data
public class UpdateBoardRequest {
    
    @NotBlank
    private long boardId;

    @NotBlank
    private String title;

    @NotBlank
    private String subtitle;

    @NotBlank
    private String markdown;

    @NotBlank
    private String html;

    private List<String> tagNames;

    @NotBlank
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
