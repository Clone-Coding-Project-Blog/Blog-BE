package com.mizzle.blogrest.service.blog;

import com.mizzle.blogrest.repository.blog.BoardRepository;
import com.mizzle.blogrest.repository.blog.LikeRepository;
import com.mizzle.blogrest.repository.blog.ReplyRepository;
import com.mizzle.blogrest.repository.blog.TagRepository;
import com.mizzle.blogrest.repository.user.UserRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final ReplyRepository replyRepository;
    private final LikeRepository likesRepository;

}
