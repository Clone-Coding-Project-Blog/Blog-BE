package com.mizzle.blogrest.controller.blog;

import com.mizzle.blogrest.config.security.token.CurrentUser;
import com.mizzle.blogrest.config.security.token.UserPrincipal;
import com.mizzle.blogrest.payload.request.blog.CreateBoardRequest;
import com.mizzle.blogrest.payload.request.blog.CreateLikeRequest;
import com.mizzle.blogrest.payload.request.blog.CreateReplyRequest;
import com.mizzle.blogrest.payload.request.blog.DeleteBoardRequest;
import com.mizzle.blogrest.payload.request.blog.DeleteLikeRequest;
import com.mizzle.blogrest.payload.request.blog.ReadBoardRequest;
import com.mizzle.blogrest.payload.request.blog.ReadBoardsRequest;
import com.mizzle.blogrest.payload.request.blog.ReadReplysRequest;
import com.mizzle.blogrest.payload.request.blog.UpdateReplyRequest;
import com.mizzle.blogrest.service.blog.BoardService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;


@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/blog")
public class BlogController {

    private final BoardService boardService;

    @GetMapping(value="/board")
    public ResponseEntity<?> readBoards(ReadBoardsRequest readBoardsRequest){
        return boardService.readBoards(readBoardsRequest);
    }

    @GetMapping(value="/board/{boardId}")
    public ResponseEntity<?> readBoard(@PathVariable long boardId){
        return boardService.readBoard(
            ReadBoardRequest.builder().boardId(boardId).build()
        );
    }

    @PostMapping(value="/board")
    public ResponseEntity<?> createBoard(@CurrentUser UserPrincipal userPrincipal, CreateBoardRequest createBoardRequest){
        return boardService.createBoard(userPrincipal, createBoardRequest);
    }

    @DeleteMapping(value="/board/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable long boardId, @CurrentUser UserPrincipal userPrincipal){
        return boardService.deleteBoard(
            userPrincipal, 
            DeleteBoardRequest.builder().boardId(boardId).build()
        );
    }

    @GetMapping(value="/reply")
    public ResponseEntity<?> readReplys(ReadReplysRequest readReplysRequest){
        return boardService.readReplys(readReplysRequest);
    }   

    @PostMapping(value="/reply")
    public ResponseEntity<?> createReply(@CurrentUser UserPrincipal userPrincipal, CreateReplyRequest createReplyRequest){
        return boardService.createReply(userPrincipal, createReplyRequest);
    }

    @PutMapping(value="/reply/{replyId}")
    public ResponseEntity<?> updateReply(@PathVariable long replyId, @CurrentUser UserPrincipal userPrincipal, UpdateReplyRequest updateReplyRequest) {
        return boardService.updateReply(
            userPrincipal, 
            UpdateReplyRequest.builder().boardId(updateReplyRequest.getBoardId()).replyId(replyId).comment(updateReplyRequest.getComment()).build()
        );
    }

    @PostMapping(value="/like/{boardId}")
    public ResponseEntity<?> createLike(@PathVariable long boardId, @CurrentUser UserPrincipal userPrincipal){
        return boardService.createLike(
            userPrincipal, 
            CreateLikeRequest.builder().boardId(boardId).build()
        );
    }
    
    @DeleteMapping(value="/like/{boardId}")
    public ResponseEntity<?> deleteLike(@PathVariable long boardId, @CurrentUser UserPrincipal userPrincipal){
        return boardService.deleteLike(
            userPrincipal, 
            DeleteLikeRequest.builder().boardId(boardId).build()
        );
    }
    
}
