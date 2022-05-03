package com.mizzle.blogrest.repository.blog;

import java.util.List;
import java.util.Optional;

import com.mizzle.blogrest.domain.entity.blog.Board;
import com.mizzle.blogrest.domain.entity.blog.Like;
import com.mizzle.blogrest.domain.entity.user.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long>{

    long countByBoard(Board board);
    long countByUser(User user);

    Optional<Like> findByBoardAndUser(Board board, User user);

    List<Like> deleteAllByUser(User user);
    List<Like> deleteAllByBoard(Board board);
    List<Like> deleteAllByUserAndBoard(User user, Board board);
}
