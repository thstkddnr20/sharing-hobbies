package com.toyproject.sh.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
public class Post {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String thumbnail;

    private String content;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private Category category;

}
