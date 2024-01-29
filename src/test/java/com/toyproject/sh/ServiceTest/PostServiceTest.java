package com.toyproject.sh.ServiceTest;

import com.toyproject.sh.domain.Category;
import com.toyproject.sh.domain.Member;
import com.toyproject.sh.domain.Post;
import com.toyproject.sh.repository.MemberRepository;
import com.toyproject.sh.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostService postService;

    @Test
    @Rollback(value = false)
    void 게시글_생성_테스트() {
        Member member = new Member();
        member.setEmail("aaa");
        memberRepository.save(member);

        System.out.println("==========================");
        Post post1 = new Post(member, "A", "content", Category.GUIDE);
        postService.createPost(post1, "#스키");


    }

    @Test
    @Rollback(value = false)
    void 게시글_전체조회() {
        Member member = new Member();
        member.setEmail("aaa");
        memberRepository.save(member);

        Post post1 = new Post(member, "A", "content1", Category.GUIDE);
        postService.createPost(post1, "#스키");
        Post post2 = new Post(member, "B", "content2", Category.GUIDE);
        postService.createPost(post2, "#스키");
        Post post3 = new Post(member, "C", "content3", Category.GUIDE);
        postService.createPost(post3, "#스키");




    }
}