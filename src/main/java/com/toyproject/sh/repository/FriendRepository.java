package com.toyproject.sh.repository;

import com.toyproject.sh.domain.Friend;
import com.toyproject.sh.domain.FriendStatus;
import com.toyproject.sh.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Query("select f.friend from Friend f where f.member =:member and f.friendStatus =:status")
    Optional<List<Member>> findAllMyFriends(@Param("member") Member member, @Param("status") FriendStatus friendStatus);

    @Query("select f from Friend f where f.member =:member and f.friend =:friend")
    Optional<Friend> findOneByMemberAndFriend(@Param("member") Member member, @Param("friend") Member friend);
}
