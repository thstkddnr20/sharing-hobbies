package com.toyproject.sh.domain;

import jakarta.persistence.*;
import lombok.Getter;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
public class TagManager {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public TagManager(Tag tag, Member member) {
        this.tag = tag;
        this.member = member;
    }

    public TagManager(Tag tag, Post post) {
        this.tag = tag;
        this.post = post;
    }

    public TagManager() {
    }
}
