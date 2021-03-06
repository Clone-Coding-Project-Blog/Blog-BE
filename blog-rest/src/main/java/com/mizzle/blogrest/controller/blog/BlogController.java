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

    @Operation(summary = "개시글(들) 불러오기", description = "개시글 정보를 불러옵니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "개시글 정보 불러오기 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = BoardMapping.class) ) } ),
        @ApiResponse(responseCode = "400", description = "개시글 정보 불러오기 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping(value="/board")
    public ResponseEntity<?> readBoards(
        @Parameter(description = "Schemas의 ReadBoardsRequest 참고해주세요.", required = true) ReadBoardsRequest readBoardsRequest
    ){
        return boardService.readBoards(readBoardsRequest);
    }

    @Operation(summary = "개시글 불러오기", description = "개시글 정보를 불러옵니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "개시글 정보 불러오기 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = BoardMapping.class) ) } ),
        @ApiResponse(responseCode = "400", description = "개시글 정보 불러오기 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping(value="/board/{boardId}")
    public ResponseEntity<?> readBoard(
        @Parameter(description = "개시글의 Id 입니다.", required = true) @PathVariable long boardId
    ){
        return boardService.readBoard(
            ReadBoardRequest.builder().boardId(boardId).build()
        );
    }

    @Operation(summary = "개시글 작성", description = "개시글을 작성 합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "개시글 작성 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class) ) } ),
        @ApiResponse(responseCode = "400", description = "개시글 작성 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping(value="/board")
    public ResponseEntity<?> createBoard(
        @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal, 
        @Parameter(description = "Schemas의 CreateBoardRequest를 참고해주세요.", required = true) @RequestBody @Validated CreateBoardRequest createBoardRequest
    ){
        return boardService.createBoard(userPrincipal, createBoardRequest);
    }

    @Operation(summary = "개시글 삭제", description = "개시글을 삭제 합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "개시글 삭제 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class) ) } ),
        @ApiResponse(responseCode = "400", description = "개시글 삭제 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @DeleteMapping(value="/board/{boardId}")
    public ResponseEntity<?> deleteBoard(
        @Parameter(description = "개시글 ID입니다.", required = true) @PathVariable long boardId, 
        @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ){
        return boardService.deleteBoard(
            userPrincipal, 
            boardId
        );
    }

    @Operation(summary = "개시글 수정", description = "개시글을 수정 합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "개시글 수정 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = BoardMapping.class) ) } ),
        @ApiResponse(responseCode = "400", description = "개시글 수정 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PutMapping(value="/board/{boardId}")
    public ResponseEntity<?> updateBoard(
        @Parameter(description = "개시글 ID입니다.", required = true) @PathVariable long boardId, 
        @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal, 
        @Parameter(description = "Schemas의 UpdateBoardRequest 참고해주세요.", required = true) @RequestBody @Validated UpdateBoardRequest updateBoardRequest
    ){
        return boardService.updateBoard(
            userPrincipal, 
            boardId,
            updateBoardRequest
        );
    }

    @Operation(summary = "댓글(들) 불러오기", description = "댓글(들)을 불러옵니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "댓글(들) 불러오기 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Reply.class) ) } ),
        @ApiResponse(responseCode = "400", description = "댓글(들) 불러오기 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @GetMapping(value="/reply/{boardId}")
    public ResponseEntity<?> readReplys(
        @Parameter(description = "개시글 ID입니다.", required = true) @PathVariable long boardId
    ){
        return boardService.readReplys(boardId);
    }

    @Operation(summary = "댓글 작성", description = "댓글을 작성합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "댓글(들) 작성 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class) ) } ),
        @ApiResponse(responseCode = "400", description = "댓글(들) 작성 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping(value="/reply/{boardId}")
    public ResponseEntity<?> createReply(
        @Parameter(description = "개시글 ID입니다.", required = true) @PathVariable long boardId, 
        @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal, 
        @Parameter(description = "Schemas의 CreateReplyRequest 참고해주세요.", required = true) @Validated @RequestBody CreateReplyRequest createReplyRequest
    ){
        return boardService.createReply(userPrincipal, boardId, createReplyRequest);
    }

    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "댓글 수정 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class) ) } ),
        @ApiResponse(responseCode = "400", description = "댓글 수정 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PutMapping(value="/reply/{boardId}/{replyId}")
    public ResponseEntity<?> updateReply(
        @Parameter(description = "개시글 ID입니다.", required = true) @PathVariable long boardId, 
        @Parameter(description = "댓글 ID입니다.", required = true) @PathVariable long replyId, 
        @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal, 
        @Parameter(description = "Schemas의 CreateReplyRequest 참고해주세요.", required = true) @Validated @RequestBody UpdateReplyRequest updateReplyRequest
    ) {
        return boardService.updateReply(
            userPrincipal, 
            boardId,
            replyId,
            UpdateReplyRequest.builder().comment(updateReplyRequest.getComment()).build()
        );
    }

    @Operation(summary = "관심버튼 생성", description = "관심버튼을 클릭(생성)합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "관심버튼 클릭 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ReadLikeResponse.class) ) } ),
        @ApiResponse(responseCode = "400", description = "관심버튼 클릭 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @PostMapping(value="/like/{boardId}")
    public ResponseEntity<?> createLike(
        @Parameter(description = "개시글 ID입니다.", required = true) @PathVariable long boardId, 
        @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ){
        return boardService.createLike(
            userPrincipal, 
            boardId
        );
    }
    
    @Operation(summary = "관심버튼 삭제", description = "관심버튼을 클릭(삭제)합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "관심버튼 클릭 성공", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ReadLikeResponse.class) ) } ),
        @ApiResponse(responseCode = "400", description = "관심버튼 클릭 실패", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class) ) } ),
    })
    @DeleteMapping(value="/like/{boardId}")
    public ResponseEntity<?> deleteLike(
        @Parameter(description = "개시글 ID입니다.", required = true) @PathVariable long boardId, 
        @Parameter(description = "Accesstoken을 입력해주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ){
        return boardService.deleteLike(
            userPrincipal, 
            boardId
        );
    }
    
}
