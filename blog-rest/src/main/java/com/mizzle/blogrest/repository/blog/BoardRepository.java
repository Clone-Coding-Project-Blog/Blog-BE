package com.mizzle.blogrest.repository.blog;


import java.util.List;
import java.util.Optional;

import com.mizzle.blogrest.domain.entity.blog.Board;
import com.mizzle.blogrest.domain.entity.blog.Purpose;
import com.mizzle.blogrest.domain.entity.user.User;
import com.mizzle.blogrest.domain.mapping.BoardMapping;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>{

    List<Board> findByTitle(String title);
    List<Board> findByUser(User user);

    Optional<Board> findByUsernameAndPurpose(String username, Purpose purpose);

    Page<BoardMapping> findByPurpose(Purpose purpose, Pageable pageable);
    Page<BoardMapping> findByPurposeAndTitleContains(Purpose purpose, String title, Pageable pageable);

    Page<BoardMapping> findByPurposeAndUsername(Purpose purpose, String username, Pageable pageable);
    Page<BoardMapping> findByPurposeAndUsernameAndTitleContains(Purpose purpose, String username, String title, Pageable pageable);

    Page<BoardMapping> findByTitleContaining(String title, Pageable pageable);

    Optional<Board> findById(long id);
    Optional<BoardMapping> findByIdAndUsername(long id, String username);

    long countByPurpose(Purpose purpose);
    long countByPurposeAndUsername(Purpose purpose, String username);

    List<Board> deleteAllByUser(User user);

}
