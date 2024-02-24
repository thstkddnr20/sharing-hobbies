package com.toyproject.sh.repository;

import com.toyproject.sh.domain.Friend;
import com.toyproject.sh.domain.FriendStatus;
import com.toyproject.sh.domain.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Query("select f.friend from Friend f where f.member.email =:email and f.friendStatus =:status") // 1번
    List<Member> findAllFriendsByStatus(@Param("email") String email, @Param("status") FriendStatus friendStatus);

    @Query("select f from Friend f where f.member =:member and f.friend =:friend")
    Optional<Friend> findOneByMemberAndFriend(@Param("member") Member member, @Param("friend") Member friend);

    //필요한것 1.(보낸사람 입장) request중인 member가져오기 2. (받은사람 입장) 본인 waiting 걸려있는거 가져오기


}
