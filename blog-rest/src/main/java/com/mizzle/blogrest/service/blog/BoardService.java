package com.mizzle.blogrest.service.blog;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import com.mizzle.blogrest.advice.assertThat.DefaultAssert;
import com.mizzle.blogrest.config.security.token.CurrentUser;
import com.mizzle.blogrest.config.security.token.UserPrincipal;
import com.mizzle.blogrest.domain.entity.blog.Board;
import com.mizzle.blogrest.domain.entity.blog.Like;
import com.mizzle.blogrest.domain.entity.blog.Purpose;
import com.mizzle.blogrest.domain.entity.blog.Reply;
import com.mizzle.blogrest.domain.entity.blog.Tag;
import com.mizzle.blogrest.domain.entity.user.User;
import com.mizzle.blogrest.domain.mapping.BoardMapping;
import com.mizzle.blogrest.payload.request.blog.ReadBoardRequest;
import com.mizzle.blogrest.payload.request.blog.ReadBoardsRequest;
import com.mizzle.blogrest.payload.request.blog.ReadReplysRequest;
import com.mizzle.blogrest.payload.request.blog.UpdateBoardRequest;
import com.mizzle.blogrest.payload.request.blog.UpdateReplyRequest;
import com.mizzle.blogrest.payload.request.blog.CreateBoardRequest;
import com.mizzle.blogrest.payload.request.blog.CreateLikeRequest;
import com.mizzle.blogrest.payload.request.blog.CreateReplyRequest;
import com.mizzle.blogrest.payload.request.blog.DeleteBoardRequest;
import com.mizzle.blogrest.payload.request.blog.DeleteLikeRequest;
import com.mizzle.blogrest.payload.request.blog.DeleteReplyRequest;
import com.mizzle.blogrest.payload.response.ApiResponse;
import com.mizzle.blogrest.payload.response.Message;
import com.mizzle.blogrest.payload.response.blog.ReadLikeResponse;
import com.mizzle.blogrest.repository.blog.BoardRepository;
import com.mizzle.blogrest.repository.blog.LikeRepository;
import com.mizzle.blogrest.repository.blog.ReplyRepository;
import com.mizzle.blogrest.repository.blog.TagRepository;
import com.mizzle.blogrest.repository.user.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final ReplyRepository replyRepository;
    private final LikeRepository likeRepository;
    
    public Board create(String title, String subtitle, String markdown, String html, User user, Purpose purpose){
        return Board.builder()
                    .title(title)
                    .subtitle(subtitle)
                    .markdown(markdown)
                    .html(html)
                    .user(user)
                    .purpose(purpose)
                    .build();
    }

    public Set<Tag> create(List<String> stringTags, Long boardId){
        Set<Tag> tags = new HashSet<Tag>();
        for (String tagName : stringTags) {
            Tag tag = Tag.builder()
                           .boardId(boardId)
                           .name(tagName)
                           .build();
            tags.add(tag);
        }
        return tags;
    }

    public Reply create(String comment, Long boardId, Long userId, String username){
        return Reply.builder()
                    .comment(comment)
                    .boardId(boardId)
                    .userId(userId)
                    .username(username)
                    .build();
    }

    public ResponseEntity<?> createBoard(@CurrentUser UserPrincipal userPrincipal, CreateBoardRequest createBoardRequest){
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다.");

        Optional<Board> savedBoard = boardRepository.findByUsernameAndPurpose(user.get().getEmail(), Purpose.write);

        long id = -1;
        if(savedBoard.isPresent()){
            id = reloadBoard(user.get(), savedBoard.get(), createBoardRequest);
        }else{
            id = createBoard(user.get(), createBoardRequest);
        }

        Optional<Board> board = boardRepository.findById(id);
        ApiResponse apiResponse = ApiResponse.builder().check(true).information(board.get()).build();

        return ResponseEntity.ok(apiResponse);
    }


    private long reloadBoard(User user, Board board, CreateBoardRequest createBoardRequest){
        board.updateAll(
            board.getId(), 
            createBoardRequest.getTitle(), 
            createBoardRequest.getSubtitle(), 
            createBoardRequest.getMarkdown(), 
            createBoardRequest.getHtml(),
            user,
            createBoardRequest.getPurpose()
        );
        boardRepository.save(board);

        tagSave(board, createBoardRequest);

        return board.getId();
    }

    private long createBoard(User user, CreateBoardRequest createBoardRequest){
        log.info("createBoardRequest={}",createBoardRequest);
        Board board = create(
            createBoardRequest.getTitle(),
            createBoardRequest.getSubtitle(),
            createBoardRequest.getMarkdown(),
            createBoardRequest.getHtml(),
            user,
            createBoardRequest.getPurpose()
        );
        boardRepository.save(board);

        tagSave(board, createBoardRequest);

        return board.getId();
    }

    private void tagSave(Board board, CreateBoardRequest writeBoardRequest) {
        tagRepository.deleteAllByBoardId(board.getId());
        Set<Tag> tags = create(writeBoardRequest.getTagNames(), board.getId());
        tagRepository.saveAll(tags);
    }

    public ResponseEntity<?> readBoards(ReadBoardsRequest readBoardRequest){
        Page<BoardMapping> boards;
        if(readBoardRequest.getSearch().isEmpty()){
            boards = boardRepository.findByPurpose(Purpose.finish, PageRequest.of(readBoardRequest.getPage(), readBoardRequest.getSize(), Sort.by(Sort.Order.desc("createdDate") )));
        }else{
            boards = boardRepository.findByPurposeAndTitleContains(Purpose.finish, readBoardRequest.getSearch(), PageRequest.of(readBoardRequest.getPage(), readBoardRequest.getSize(), Sort.by(Sort.Order.desc("createdDate"))) );
        }
        log.info("{}", boards.getContent());
        ApiResponse apiResponse = ApiResponse.builder().check(true).information(boards.getContent()).build();
        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> readBoard(ReadBoardRequest readBoardRequest){
        Optional<Board> board = boardRepository.findById(readBoardRequest.getBoardId());
        DefaultAssert.isTrue(board.isPresent(), "해당 개시글이 존재하지 않습니다.");

        ApiResponse apiResponse = ApiResponse.builder().check(true).information(board.get()).build();
        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> updateBoard(@CurrentUser UserPrincipal userPrincipal, UpdateBoardRequest updateBoardRequest){
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다.");

        Optional<Board> board = boardRepository.findByIdAndUsername(updateBoardRequest.getBoardId(), user.get().getEmail());
        DefaultAssert.isTrue(board.isPresent(), "해당 개시글을 해당 유저가 수정할 수 없습니다.");

        long id = reloadBoard(
            user.get(), 
            board.get(), 
            CreateBoardRequest.builder()
                            .title(updateBoardRequest.getTitle())
                            .subtitle(updateBoardRequest.getSubtitle())
                            .markdown(updateBoardRequest.getMarkdown())
                            .html(updateBoardRequest.getHtml())
                            .purpose(updateBoardRequest.getPurpose())
                            .build()
        );
        ApiResponse apiResponse = ApiResponse.builder().check(true).information(board.get()).build();
        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> createReply(@CurrentUser UserPrincipal userPrincipal, CreateReplyRequest createReplyRequest){
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다.");

        Optional<Board> board = boardRepository.findById(createReplyRequest.getBoardId());
        DefaultAssert.isTrue(board.isPresent(), "개시글 정보가 올바르지 않습니다.");

        Reply reply = create(
            createReplyRequest.getComment(),
            board.get().getId(),
            user.get().getId(),
            user.get().getEmail()
        );

        replyRepository.save(reply);
        List<Reply> replys = replyRepository.findByBoardId(board.get().getId());

        ApiResponse apiResponse = ApiResponse.builder().check(true).information(replys).build();
        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> readReplys(ReadReplysRequest readReplysRequest){

        Optional<Board> board = boardRepository.findById(readReplysRequest.getBoardId());
        DefaultAssert.isTrue(board.isPresent(), "개시글 정보가 올바르지 않습니다.");

        List<Reply> replys = replyRepository.findByBoardId(board.get().getId());

        ApiResponse apiResponse = ApiResponse.builder().check(true).information(replys).build();
        return ResponseEntity.ok(apiResponse);
    }

    @Transactional
    public ResponseEntity<?> updateReply(@CurrentUser UserPrincipal userPrincipal, UpdateReplyRequest updateReplyRequest){
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다.");

        Optional<Reply> reply = replyRepository.findById(updateReplyRequest.getReplyId());

        reply.get().updateComment(updateReplyRequest.getComment());
        replyRepository.save(reply.get());

        ApiResponse apiResponse = ApiResponse.builder().check(true).information(reply.get()).build();
        return ResponseEntity.ok(apiResponse);
    }

    @Transactional
    public ResponseEntity<?> deleteBoard(@CurrentUser UserPrincipal userPrincipal, DeleteBoardRequest deleteBoardRequest){
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다.");

        Optional<Board> board = boardRepository.findByIdAndUsername(deleteBoardRequest.getBoardId(), user.get().getEmail());
        DefaultAssert.isTrue(board.isPresent(), "개시글 아이디가 올바르지 않습니다.");

        boardRepository.delete(board.get());

        ApiResponse apiResponse = ApiResponse.builder().check(true).information(
            Message.builder().message("해당 개시글이 삭제되었습니다.").build()
        ).build();
        return ResponseEntity.ok(apiResponse);
    }
    
    @Transactional
    public ResponseEntity<?> deleteReply(@CurrentUser UserPrincipal userPrincipal, DeleteReplyRequest deleteReplyRequest){
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다.");

        Optional<Reply> reply = replyRepository.findByIdAndBoardId(deleteReplyRequest.getReplyId(), deleteReplyRequest.getBoardId());
        DefaultAssert.isTrue(reply.isPresent(), "댓글이 존재하지 않습니다.");

        replyRepository.delete(reply.get());

        ApiResponse apiResponse = ApiResponse.builder().check(true).information(
            Message.builder().message("해당 댓글이 삭제되었습니다.").build()
        ).build();
        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> createLike(@CurrentUser UserPrincipal userPrincipal, CreateLikeRequest createLikeRequest){
        boolean likeCheck = false;

        Optional<User> user = userRepository.findById(userPrincipal.getId());
        Optional<Board> board = boardRepository.findById(createLikeRequest.getBoardId());
        likeCheck = true;

        Optional<Like> like = likeRepository.findByBoardAndUser(board.get(), user.get());
        if(like.isPresent()){
            likeRepository.save(
                Like.builder().user(user.get()).board(board.get()).build()
            );
        }
        
        long count = likeRepository.countByBoard(board.get());

        ApiResponse apiResponse = ApiResponse.builder().check(true).information(
            ReadLikeResponse.builder().likeCheck(likeCheck).count(count).build()
        ).build();

        return ResponseEntity.ok(apiResponse);
    }

    @Transactional
    public ResponseEntity<?> deleteLike(@CurrentUser UserPrincipal userPrincipal, DeleteLikeRequest deleteLikeRequest){
        boolean likeCheck = false;
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        Optional<Board> board = boardRepository.findById(deleteLikeRequest.getBoardId());

        Optional<Like> like = likeRepository.findByBoardAndUser(board.get(), user.get());
        likeRepository.delete(like.get());

        long count = likeRepository.countByBoard(board.get());

        ApiResponse apiResponse = ApiResponse.builder().check(true).information(
            ReadLikeResponse.builder().likeCheck(likeCheck).count(count).build()
        ).build();

        return ResponseEntity.ok(apiResponse);
    }
}
