package com.mizzle.blogrest.domain.entity.blog;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.mizzle.blogrest.domain.entity.time.DefaultTime;
import com.mizzle.blogrest.domain.entity.user.User;

import org.hibernate.annotations.DynamicUpdate;

import lombok.Builder;
import lombok.Getter;

@DynamicUpdate
@Getter
@Entity
@Table(name = "board")
public class Board extends DefaultTime{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    //@Id
    @Column(name="title", nullable=false)
    private String title;

    //@Id
    @Column(name="username", nullable=false)
    private String username;

    @Column(nullable=false)
    private String subtitle;

    @Column(nullable=false)
    private boolean visiable;

    @Column(columnDefinition = "LONGTEXT", nullable=false)
    private String markdown;
    
    @Column(columnDefinition = "LONGTEXT", nullable=false)
    private String html;

    @Enumerated(EnumType.STRING)
    private Purpose purpose;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    //@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OneToMany(cascade = CascadeType.REMOVE, fetch=FetchType.EAGER)
    @JoinColumn(name = "board_id")
    private Set<Tag> tag = new HashSet<>();

    @OneToMany(cascade = CascadeType.REMOVE, fetch=FetchType.EAGER)
    @JoinColumn(name = "board_id")
    private Set<Reply> reply = new HashSet<>();

    public Board(){}

    @Builder
    public Board(String title, String subtitle, String markdown, String html, User user, Purpose purpose){
        this.title = title;
        this.markdown = markdown;
        this.subtitle = subtitle;
        this.html = html;
        this.user = user;
        this.username = user.getEmail();
        this.purpose = purpose;
    }

    public void updateId(long id){
        this.id = id;
    }

    public void updatePurpose(Purpose purpose){
        this.purpose = purpose;
    }

    public void updateTags(Set<Tag> tag){
        this.tag = tag;
    }

    public void updateAll(long id, String title, String subtitle, String markdown, String html, User user, Purpose purpose){
        this.id = id;
        this.title = title;
        this.markdown = markdown;
        this.subtitle = subtitle;
        this.html = html;
        this.user = user;
        this.username = user.getEmail();
        this.purpose = purpose;
    }
}
