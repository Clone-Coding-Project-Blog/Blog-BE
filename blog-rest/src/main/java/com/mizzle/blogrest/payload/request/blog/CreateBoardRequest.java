package com.mizzle.blogrest.payload.request.blog;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.mizzle.blogrest.domain.entity.blog.Purpose;

import lombok.Data;

@Data
public class CreateBoardRequest {

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

}
