package com.toyproject.sh.domain;

import jakarta.persistence.*;

@Entity
public class Friend {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "friend_id")
    private Member friend;

    @Enumerated(EnumType.STRING)
    private FriendStatus friendStatus;
}
