package com.mizzle.blogrest.service.blog;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.mizzle.blogrest.advice.assertThat.DefaultAssert;
import com.mizzle.blogrest.config.security.token.CurrentUser;
import com.mizzle.blogrest.config.security.token.UserPrincipal;
import com.mizzle.blogrest.domain.entity.blog.Board;
import com.mizzle.blogrest.domain.entity.blog.Purpose;
import com.mizzle.blogrest.domain.entity.blog.Reply;
import com.mizzle.blogrest.domain.entity.blog.Tag;
import com.mizzle.blogrest.domain.entity.user.User;
import com.mizzle.blogrest.domain.mapping.BoardMapping;
import com.mizzle.blogrest.payload.request.blog.ReadBoardRequest;
import com.mizzle.blogrest.payload.request.blog.ReadBoardsRequest;
import com.mizzle.blogrest.payload.request.blog.ReadReplysRequest;
import com.mizzle.blogrest.payload.request.blog.UpdateReplyRequest;
import com.mizzle.blogrest.payload.Message;
import com.mizzle.blogrest.payload.request.blog.CreateBoardRequest;
import com.mizzle.blogrest.payload.request.blog.CreateReplyRequest;
import com.mizzle.blogrest.payload.request.blog.DeleteBoardRequest;
import com.mizzle.blogrest.payload.request.blog.DeleteReplyRequest;
import com.mizzle.blogrest.payload.response.ApiResponse;
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

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final ReplyRepository replyRepository;
    private final LikeRepository likesRepository;
    
    /**
     * board 생성자 입니다.
     * @param title
     * @param subtitle
     * @param markdown
     * @param html
     * @param user
     * @param purpose
     * @return
     */
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

    /**
     * Tag 목록들 생성자 입니다.
     * @param stringTags
     * @param boardId
     * @return
     */
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

    /**
     * 댓글 코드 생성자 입니다.
     * @param comment
     * @param boardId
     * @param userId
     * @param username
     * @return
     */
    public Reply create(String comment, Long boardId, Long userId, String username){
        return Reply.builder()
                    .comment(comment)
                    .boardId(boardId)
                    .userId(userId)
                    .username(username)
                    .build();
    }

    public ResponseEntity<?> createBoard(@CurrentUser UserPrincipal userPrincipal, CreateBoardRequest writeBoardRequest){
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다.");

        Optional<Board> savedBoard = boardRepository.findByUsernameAndPurpose(user.get().getEmail(), Purpose.write);

        long id = -1;
        if(savedBoard.isPresent()){
            id = reloadBoard(user.get(), savedBoard.get(), writeBoardRequest);
        }else{
            id = createBoard(user.get(), writeBoardRequest);
        }

        Optional<Board> board = boardRepository.findById(id);
        ApiResponse apiResponse = ApiResponse.builder().check(true).information(board.get()).build();

        return ResponseEntity.ok(apiResponse);
    }

    private long reloadBoard(User user, Board board, CreateBoardRequest writeBoardRequest){
        board.updateAll(
            board.getId(), 
            writeBoardRequest.getTitle(), 
            writeBoardRequest.getSubtitle(), 
            writeBoardRequest.getMarkdown(), 
            writeBoardRequest.getHtml(),
            user,
            writeBoardRequest.getPurpose()
        );
        boardRepository.save(board);

        tagSave(board, writeBoardRequest);

        return board.getId();
    }

    private long createBoard(User user, CreateBoardRequest writeBoardRequest){
        Board board = create(
            writeBoardRequest.getTitle(),
            writeBoardRequest.getSubtitle(),
            writeBoardRequest.getMarkdown(),
            writeBoardRequest.getHtml(),
            user,
            writeBoardRequest.getPurpose()
        );
        boardRepository.save(board);

        tagSave(board, writeBoardRequest);

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

        ApiResponse apiResponse = ApiResponse.builder().check(true).information(boards.getContent()).build();
        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> readBoard(ReadBoardRequest readBoardRequest){
        Optional<Board> board = boardRepository.findById(readBoardRequest.getBoardId());
        DefaultAssert.isTrue(board.isPresent(), "해당 개시글이 존재하지 않습니다.");

        ApiResponse apiResponse = ApiResponse.builder().check(true).information(board.get()).build();
        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> createReply(@CurrentUser UserPrincipal userPrincipal, CreateReplyRequest createReplyRequest){
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다.");

        Optional<Board> board = boardRepository.findById(createReplyRequest.getBoardId());
        DefaultAssert.isTrue(board.isPresent(), "개시글 정보가 올바르지 않습니다.");

        //reply 작성
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

        ApiResponse apiResponse = ApiResponse.builder().check(true).information(Message.builder().message("해당 개시글이 삭제되었습니다.").build()).build();
        return ResponseEntity.ok(apiResponse);
    }

    
    @Transactional
    public ResponseEntity<?> deleteReply(@CurrentUser UserPrincipal userPrincipal, DeleteReplyRequest deleteReplyRequest){
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다.");

        Optional<Reply> reply = replyRepository.findByIdAndBoardId(deleteReplyRequest.getReplyId(), deleteReplyRequest.getBoardId());
        DefaultAssert.isTrue(reply.isPresent(), "댓글이 존재하지 않습니다.");

        replyRepository.delete(reply.get());

        ApiResponse apiResponse = ApiResponse.builder().check(true).information(Message.builder().message("해당 댓글이 삭제되었습니다.").build()).build();
        return ResponseEntity.ok(apiResponse);
    }
}
