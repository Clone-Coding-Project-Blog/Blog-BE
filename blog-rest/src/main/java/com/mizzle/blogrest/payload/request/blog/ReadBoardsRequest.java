package com.mizzle.blogrest.payload.request.blog;


import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ReadBoardsRequest {
    private String search;
    
    @NotBlank
    private int page;
    
    @NotBlank
    private int size;
}
