package com.toyproject.sh.ServiceTest;

import com.toyproject.sh.domain.Friend;
import com.toyproject.sh.domain.FriendStatus;
import com.toyproject.sh.domain.Member;
import com.toyproject.sh.repository.FriendRepository;
import com.toyproject.sh.service.MemberService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    FriendRepository friendRepository;

    @Test
    void duplicateTest(){
        Member member1 = new Member();
        member1.setEmail("thstkddnr20@naver.com");

        Member member2 = new Member();
        member2.setEmail("thstkddnr20@naver.com");

        memberService.join(member1);
        memberService.join(member2);
    }

    @Test
    @Transactional
    @Rollback(value = false)
    void findFriend(){
        Member member1 = new Member();
        member1.setEmail("thstkddnr20@naver.com");
        memberService.join(member1);

        Member member2 = new Member();
        member2.setEmail("thstkddnr200@naver.com");
        memberService.join(member2);

        System.out.println("member1 = " + member1);
        System.out.println("member2 = " + member2);



        memberService.requestFriend(member1, member2);
        memberService.acceptFriend(member2, member1);

        List<Member> friends1 = memberService.findFriends(member1);
        List<Member> friends2 = memberService.findFriends(member2);
        System.out.println("friends1 = " + friends1);
        System.out.println("friends2 = " + friends2);


    }

}
