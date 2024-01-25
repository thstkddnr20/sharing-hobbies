package com.toyproject.sh.repository;

import com.toyproject.sh.domain.Friend;
import com.toyproject.sh.domain.FriendStatus;
import com.toyproject.sh.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Query("select f.friend from Friend f where f.member =:member and f.friendStatus =:status")
    List<Member> findAllMyFriends(@Param("member") Member member, @Param("status") FriendStatus friendStatus);
}
