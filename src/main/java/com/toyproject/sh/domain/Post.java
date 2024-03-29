package com.toyproject.sh.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@Setter
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long count; // 실제로 게시글에 채번되는 번호

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String thumbnail;

    private String content;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<TagManager> tagManagers;

    public Post(Member member, String thumbnail, String content, Category category) {
        this.member = member;
        this.thumbnail = thumbnail;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.category = category;
    }

    public Post(Long id, Member member, String thumbnail, String content, Category category) {
        this.id = id;
        this.member = member;
        this.thumbnail = thumbnail;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.category = category;
    }

    public Post() {
    }
}
