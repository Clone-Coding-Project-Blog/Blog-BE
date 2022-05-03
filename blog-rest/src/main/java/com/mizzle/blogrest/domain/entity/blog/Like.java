package com.mizzle.blogrest.domain.entity.blog;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.mizzle.blogrest.domain.entity.user.User;

import org.hibernate.annotations.DynamicUpdate;

import lombok.Builder;
import lombok.Getter;

@DynamicUpdate
@Getter
@Entity
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "board")
    private Board board;
    
    public Like(){
        
    }

    @Builder
    public Like(User user, Board board){
        this.user = user;
        this.board = board;
    }

}
