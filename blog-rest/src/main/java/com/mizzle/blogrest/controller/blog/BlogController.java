package com.mizzle.blogrest.controller.blog;

import com.mizzle.blogrest.service.blog.BoardService;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class BlogController {

    private final BoardService boardService;

    
}
