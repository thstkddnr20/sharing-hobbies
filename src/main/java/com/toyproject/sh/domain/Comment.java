package com.toyproject.sh.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String content;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "reply")
    private List<Comment> parentComment;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parentComment_id")
    private Comment reply;

    public Comment(Member member, Post post, String content) {
        this.member = member;
        this.post = post;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public Comment() {
    }
}
