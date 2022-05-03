package com.mizzle.blogrest.repository.blog;

import java.util.List;
import java.util.Optional;

import com.mizzle.blogrest.domain.entity.blog.Reply;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long>{
    List<Reply> findByBoardId(long boardId);
    Optional<Reply> findByIdAndBoardId(long id, long boardId);
    List<Reply> deleteAllByBoardId(long boardId);
    List<Reply> deleteAllByUserId(long userId);
    
}
