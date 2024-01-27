package com.toyproject.sh.ServiceTest;

import com.toyproject.sh.domain.Member;
import com.toyproject.sh.repository.FriendRepository;
import com.toyproject.sh.service.MemberService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;


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
        member1.setEmail("1");
        memberService.join(member1);

        Member member2 = new Member();
        member2.setEmail("2");
        memberService.join(member2);

        Member member3 = new Member();
        member3.setEmail("3");
        memberService.join(member3);

        Member member4 = new Member();
        member4.setEmail("4");
        memberService.join(member4);

        Member member5 = new Member();
        member5.setEmail("5");
        memberService.join(member5);
        System.out.println("=============================");
        memberService.requestFriend(member2, member1);
        System.out.println("=============================");
        memberService.requestFriend(member3, member1);
        memberService.requestFriend(member4, member1);
        memberService.requestFriend(member5, member1);
        System.out.println("=============================");
        memberService.acceptFriend(member1, member2);
        System.out.println("=============================");
        memberService.acceptFriend(member1, member3);
        memberService.acceptFriend(member1, member4);
        memberService.acceptFriend(member1, member5);
        System.out.println("=============================");
        List<Member> friends = memberService.findFriends(member1, 1); // 친구가 더 없는 페이지가 뜰 경우 "친구가 없습니다" 오류발생
        System.out.println("=============================");
        System.out.println("friends = " + friends);


    }

    @Test
    @Transactional
    @Rollback(value = false)
    void tagMember(){
        Member member1 = new Member();
        member1.setEmail("thstkddnr20@naver.com");
        memberService.join(member1);

        Member member2 = new Member();
        member2.setEmail("thstkddnr200@naver.com");
        memberService.join(member2);

        System.out.println("member1 = " + member1);
        System.out.println("member2 = " + member2);

        System.out.println("=============================");
        memberService.addMemberTag("#스키", member1);
        memberService.addMemberTag("#수영", member1);
        memberService.addMemberTag("#클라이밍", member1);
        System.out.println("=============================");
        List<String> tag1 = memberService.findTag(member1);
        System.out.println("tag1 = " + tag1);
        System.out.println("=============================");
        memberService.deleteMemberTag("#스키", member1);
        List<String> tag2 = memberService.findTag(member1);
        System.out.println("tag2 = " + tag2);


    }

}
