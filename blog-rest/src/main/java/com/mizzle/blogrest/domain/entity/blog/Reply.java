package com.mizzle.blogrest.domain.entity.blog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.mizzle.blogrest.domain.entity.time.DefaultTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Table(name = "reply")
public class Reply extends DefaultTime{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String comment;

    @Column
    private String username;
    
    @Column(name = "board_id")
    private Long boardId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "reply_id")
    private Long replyId;

    public Reply(){

    }

    @Builder
    public Reply(String comment, Long boardId, Long userId, String username){
        this.comment = comment;
        this.boardId = boardId;
        this.userId = userId;
        this.username = username;
    }

    public void updateComment(String comment){
        this.comment = comment;
    }

    @Override
    public String toString() {
        return this.comment;
    }
}
