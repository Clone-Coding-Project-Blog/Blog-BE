package com.mizzle.blogrest.repository.blog;

import java.util.List;

import com.mizzle.blogrest.domain.entity.blog.Tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long>{
    List<Tag> findByBoardId(long boardId);
    List<Tag> deleteAllByBoardId(long boardId);
    long countByBoardId(long boardId);
}
