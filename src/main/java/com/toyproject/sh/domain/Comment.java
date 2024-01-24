package com.toyproject.sh.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Comment {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private String content;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "reply")
    private List<Comment> parentComment;

    @ManyToOne
    @JoinColumn(name = "parentComment_id")
    private Comment reply;
}
