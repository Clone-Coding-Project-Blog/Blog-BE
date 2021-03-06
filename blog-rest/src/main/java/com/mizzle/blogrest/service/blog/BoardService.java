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
import com.mizzle.blogrest.payload.request.blog.UpdateBoardRequest;
import com.mizzle.blogrest.payload.request.blog.UpdateReplyRequest;
import com.mizzle.blogrest.payload.request.blog.CreateBoardRequest;
import com.mizzle.blogrest.payload.request.blog.CreateReplyRequest;
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
        log.info("createBoardRequest={}",createBoardRequest);

        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "????????? ???????????? ????????????.");

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

    @Transactional
    private long reloadBoard(User user, Board board, CreateBoardRequest createBoardRequest){
        log.info("updateBoardRequest={}",createBoardRequest);

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

    @Transactional
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

    @Transactional
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
        DefaultAssert.isTrue(board.isPresent(), "?????? ???????????? ???????????? ????????????.");

        ApiResponse apiResponse = ApiResponse.builder().check(true).information(board.get()).build();
        return ResponseEntity.ok(apiResponse);
    }

    @Transactional
    public ResponseEntity<?> updateBoard(@CurrentUser UserPrincipal userPrincipal,long boardId,  UpdateBoardRequest updateBoardRequest){
        log.info("updateBoardRequest={}",updateBoardRequest);
        
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "????????? ???????????? ????????????.");

        Optional<Board> board = boardRepository.findByIdAndUsername(boardId, user.get().getEmail());
        DefaultAssert.isTrue(board.isPresent(), "?????? ???????????? ?????? ????????? ????????? ??? ????????????.");

        long id = reloadBoard(
            user.get(), 
            board.get(), 
            CreateBoardRequest.builder()
                            .title(updateBoardRequest.getTitle())
                            .subtitle(updateBoardRequest.getSubtitle())
                            .markdown(updateBoardRequest.getMarkdown())
                            .html(updateBoardRequest.getHtml())
                            .purpose(updateBoardRequest.getPurpose())
                            .tagNames(updateBoardRequest.getTagNames())
                            .build()
        );
        ApiResponse apiResponse = ApiResponse.builder().check(true).information(board.get()).build();
        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> createReply(@CurrentUser UserPrincipal userPrincipal, long boardId, CreateReplyRequest createReplyRequest){
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "????????? ???????????? ????????????.");

        Optional<Board> board = boardRepository.findById(boardId);
        DefaultAssert.isTrue(board.isPresent(), "????????? ????????? ???????????? ????????????.");

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

    public ResponseEntity<?> readReplys(long boardId){

        Optional<Board> board = boardRepository.findById(boardId);
        DefaultAssert.isTrue(board.isPresent(), "????????? ????????? ???????????? ????????????.");

        List<Reply> replys = replyRepository.findByBoardId(board.get().getId());

        ApiResponse apiResponse = ApiResponse.builder().check(true).information(replys).build();
        return ResponseEntity.ok(apiResponse);
    }

    @Transactional
    public ResponseEntity<?> updateReply(@CurrentUser UserPrincipal userPrincipal, long boardId, long replyId, UpdateReplyRequest updateReplyRequest){
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "????????? ???????????? ????????????.");

        Optional<Reply> reply = replyRepository.findById(replyId);

        reply.get().updateComment(updateReplyRequest.getComment());
        replyRepository.save(reply.get());

        ApiResponse apiResponse = ApiResponse.builder().check(true).information(reply.get()).build();
        return ResponseEntity.ok(apiResponse);
    }

    @Transactional
    public ResponseEntity<?> deleteBoard(@CurrentUser UserPrincipal userPrincipal, long boardId){
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "????????? ???????????? ????????????.");

        Optional<Board> board = boardRepository.findByIdAndUsername(boardId, user.get().getEmail());
        DefaultAssert.isTrue(board.isPresent(), "????????? ???????????? ???????????? ????????????.");

        boardRepository.delete(board.get());

        ApiResponse apiResponse = ApiResponse.builder().check(true).information(
            Message.builder().message("?????? ???????????? ?????????????????????.").build()
        ).build();
        return ResponseEntity.ok(apiResponse);
    }
    
    @Transactional
    public ResponseEntity<?> deleteReply(@CurrentUser UserPrincipal userPrincipal, DeleteReplyRequest deleteReplyRequest){
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "????????? ???????????? ????????????.");

        Optional<Reply> reply = replyRepository.findByIdAndBoardId(deleteReplyRequest.getReplyId(), deleteReplyRequest.getBoardId());
        DefaultAssert.isTrue(reply.isPresent(), "????????? ???????????? ????????????.");

        replyRepository.delete(reply.get());

        ApiResponse apiResponse = ApiResponse.builder().check(true).information(
            Message.builder().message("?????? ????????? ?????????????????????.").build()
        ).build();
        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> createLike(@CurrentUser UserPrincipal userPrincipal, long boardId){
        boolean likeCheck = false;

        Optional<User> user = userRepository.findById(userPrincipal.getId());
        Optional<Board> board = boardRepository.findById(boardId);
        likeCheck = true;

        Optional<Like> like = likeRepository.findByBoardAndUser(board.get(), user.get());
        if(!like.isPresent()){
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
    public ResponseEntity<?> deleteLike(@CurrentUser UserPrincipal userPrincipal, long boardId){
        boolean likeCheck = false;
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        Optional<Board> board = boardRepository.findById(boardId);

        Optional<Like> like = likeRepository.findByBoardAndUser(board.get(), user.get());
        likeRepository.delete(like.get());

        long count = likeRepository.countByBoard(board.get());

        ApiResponse apiResponse = ApiResponse.builder().check(true).information(
            ReadLikeResponse.builder().likeCheck(likeCheck).count(count).build()
        ).build();

        return ResponseEntity.ok(apiResponse);
    }
}
