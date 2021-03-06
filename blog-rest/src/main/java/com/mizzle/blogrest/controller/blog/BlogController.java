package com.mizzle.blogrest.controller.blog;

import com.mizzle.blogrest.advice.payload.ErrorResponse;
import com.mizzle.blogrest.config.security.token.CurrentUser;
import com.mizzle.blogrest.config.security.token.UserPrincipal;
import com.mizzle.blogrest.domain.entity.blog.Reply;
import com.mizzle.blogrest.domain.mapping.BoardMapping;
import com.mizzle.blogrest.payload.request.blog.CreateBoardRequest;
import com.mizzle.blogrest.payload.request.blog.CreateReplyRequest;
import com.mizzle.blogrest.payload.request.blog.ReadBoardRequest;
import com.mizzle.blogrest.payload.request.blog.ReadBoardsRequest;
import com.mizzle.blogrest.payload.request.blog.UpdateBoardRequest;
import com.mizzle.blogrest.payload.request.blog.UpdateReplyRequest;
import com.mizzle.blogrest.payload.response.Message;
import com.mizzle.blogrest.payload.response.blog.ReadLikeResponse;
import com.mizzle.blogrest.service.blog.BoardService;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Tag(name = "Blog", description = "Blog API")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/blog")
public class BlogController {

    private final BoardService boardService;

    @Operation(summary = "?????????(???) ????????????", description = "????????? ????????? ???????????????.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "????????? ?????? ???????????? ??????", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = BoardMapping.class) ) } ),
        @ApiResponse(responseCode = "400", description = "????????? ?????? ???????????? ??????", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping(value="/board")
    public ResponseEntity<?> readBoards(
        @Parameter(description = "Schemas??? ReadBoardsRequest ??????????????????.", required = true) ReadBoardsRequest readBoardsRequest
    ){
        return boardService.readBoards(readBoardsRequest);
    }

    @Operation(summary = "????????? ????????????", description = "????????? ????????? ???????????????.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "????????? ?????? ???????????? ??????", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = BoardMapping.class) ) } ),
        @ApiResponse(responseCode = "400", description = "????????? ?????? ???????????? ??????", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping(value="/board/{boardId}")
    public ResponseEntity<?> readBoard(
        @Parameter(description = "???????????? Id ?????????.", required = true) @PathVariable long boardId
    ){
        return boardService.readBoard(
            ReadBoardRequest.builder().boardId(boardId).build()
        );
    }

    @Operation(summary = "????????? ??????", description = "???????????? ?????? ?????????.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "????????? ?????? ??????", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class) ) } ),
        @ApiResponse(responseCode = "400", description = "????????? ?????? ??????", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping(value="/board")
    public ResponseEntity<?> createBoard(
        @Parameter(description = "Accesstoken??? ??????????????????.", required = true) @CurrentUser UserPrincipal userPrincipal, 
        @Parameter(description = "Schemas??? CreateBoardRequest??? ??????????????????.", required = true) @RequestBody @Validated CreateBoardRequest createBoardRequest
    ){
        return boardService.createBoard(userPrincipal, createBoardRequest);
    }

    @Operation(summary = "????????? ??????", description = "???????????? ?????? ?????????.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "????????? ?????? ??????", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class) ) } ),
        @ApiResponse(responseCode = "400", description = "????????? ?????? ??????", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @DeleteMapping(value="/board/{boardId}")
    public ResponseEntity<?> deleteBoard(
        @Parameter(description = "????????? ID?????????.", required = true) @PathVariable long boardId, 
        @Parameter(description = "Accesstoken??? ??????????????????.", required = true) @CurrentUser UserPrincipal userPrincipal
    ){
        return boardService.deleteBoard(
            userPrincipal, 
            boardId
        );
    }

    @Operation(summary = "????????? ??????", description = "???????????? ?????? ?????????.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "????????? ?????? ??????", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = BoardMapping.class) ) } ),
        @ApiResponse(responseCode = "400", description = "????????? ?????? ??????", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PutMapping(value="/board/{boardId}")
    public ResponseEntity<?> updateBoard(
        @Parameter(description = "????????? ID?????????.", required = true) @PathVariable long boardId, 
        @Parameter(description = "Accesstoken??? ??????????????????.", required = true) @CurrentUser UserPrincipal userPrincipal, 
        @Parameter(description = "Schemas??? UpdateBoardRequest ??????????????????.", required = true) @RequestBody @Validated UpdateBoardRequest updateBoardRequest
    ){
        return boardService.updateBoard(
            userPrincipal, 
            boardId,
            updateBoardRequest
        );
    }

    @Operation(summary = "??????(???) ????????????", description = "??????(???)??? ???????????????.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "??????(???) ???????????? ??????", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Reply.class) ) } ),
        @ApiResponse(responseCode = "400", description = "??????(???) ???????????? ??????", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping(value="/reply/{boardId}")
    public ResponseEntity<?> readReplys(
        @Parameter(description = "????????? ID?????????.", required = true) @PathVariable long boardId
    ){
        return boardService.readReplys(boardId);
    }

    @Operation(summary = "?????? ??????", description = "????????? ???????????????.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "??????(???) ?????? ??????", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class) ) } ),
        @ApiResponse(responseCode = "400", description = "??????(???) ?????? ??????", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping(value="/reply/{boardId}")
    public ResponseEntity<?> createReply(
        @Parameter(description = "????????? ID?????????.", required = true) @PathVariable long boardId, 
        @Parameter(description = "Accesstoken??? ??????????????????.", required = true) @CurrentUser UserPrincipal userPrincipal, 
        @Parameter(description = "Schemas??? CreateReplyRequest ??????????????????.", required = true) @Validated @RequestBody CreateReplyRequest createReplyRequest
    ){
        return boardService.createReply(userPrincipal, boardId, createReplyRequest);
    }

    @Operation(summary = "?????? ??????", description = "????????? ???????????????.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "?????? ?????? ??????", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class) ) } ),
        @ApiResponse(responseCode = "400", description = "?????? ?????? ??????", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PutMapping(value="/reply/{boardId}/{replyId}")
    public ResponseEntity<?> updateReply(
        @Parameter(description = "????????? ID?????????.", required = true) @PathVariable long boardId, 
        @Parameter(description = "?????? ID?????????.", required = true) @PathVariable long replyId, 
        @Parameter(description = "Accesstoken??? ??????????????????.", required = true) @CurrentUser UserPrincipal userPrincipal, 
        @Parameter(description = "Schemas??? CreateReplyRequest ??????????????????.", required = true) @Validated @RequestBody UpdateReplyRequest updateReplyRequest
    ) {
        return boardService.updateReply(
            userPrincipal, 
            boardId,
            replyId,
            UpdateReplyRequest.builder().comment(updateReplyRequest.getComment()).build()
        );
    }

    @Operation(summary = "???????????? ??????", description = "??????????????? ??????(??????)?????????.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "???????????? ?????? ??????", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ReadLikeResponse.class) ) } ),
        @ApiResponse(responseCode = "400", description = "???????????? ?????? ??????", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping(value="/like/{boardId}")
    public ResponseEntity<?> createLike(
        @Parameter(description = "????????? ID?????????.", required = true) @PathVariable long boardId, 
        @Parameter(description = "Accesstoken??? ??????????????????.", required = true) @CurrentUser UserPrincipal userPrincipal
    ){
        return boardService.createLike(
            userPrincipal, 
            boardId
        );
    }
    
    @Operation(summary = "???????????? ??????", description = "??????????????? ??????(??????)?????????.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "???????????? ?????? ??????", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ReadLikeResponse.class) ) } ),
        @ApiResponse(responseCode = "400", description = "???????????? ?????? ??????", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @DeleteMapping(value="/like/{boardId}")
    public ResponseEntity<?> deleteLike(
        @Parameter(description = "????????? ID?????????.", required = true) @PathVariable long boardId, 
        @Parameter(description = "Accesstoken??? ??????????????????.", required = true) @CurrentUser UserPrincipal userPrincipal
    ){
        return boardService.deleteLike(
            userPrincipal, 
            boardId
        );
    }
    
}
