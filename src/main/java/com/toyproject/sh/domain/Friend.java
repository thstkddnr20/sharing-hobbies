package com.toyproject.sh.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Friend {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "friend_id")
    private Member friend;

    @Enumerated(EnumType.STRING)
    private FriendStatus friendStatus;

    public Friend(Member member, Member friend, FriendStatus friendStatus) {
        this.member = member;
        this.friend = friend;
        this.friendStatus = friendStatus;
    }

    public static List<Friend> requestAdd(Member member, Member friend){ // 친구 추가 생성메서드
        Friend friend1 = new Friend(member, friend, FriendStatus.REQUEST);
        Friend friend2 = new Friend(friend, member, FriendStatus.WAITING);
        List<Friend> friendList = new ArrayList<>();
        friendList.add(friend1);
        friendList.add(friend2);
        return friendList;
    }
}
