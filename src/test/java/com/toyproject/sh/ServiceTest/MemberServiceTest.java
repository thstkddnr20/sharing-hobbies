package com.toyproject.sh.ServiceTest;

import com.toyproject.sh.domain.Member;
import com.toyproject.sh.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    void duplicateTest(){
        Member member1 = new Member();
        member1.setEmail("thstkddnr20@naver.com");

        Member member2 = new Member();
        member2.setEmail("thstkddnr20@naver.com");

        memberService.join(member1);
        memberService.join(member2);
    }

}
