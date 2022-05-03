package com.mizzle.blogrest.controller.blog;

import com.mizzle.blogrest.service.blog.BoardService;
import com.mizzle.blogrest.service.user.UserService;

import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class BlogController {

    private final UserService userService;
    private final BoardService boardService;

    
}
