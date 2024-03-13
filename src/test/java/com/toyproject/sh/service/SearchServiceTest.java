package com.toyproject.sh.service;

import com.toyproject.sh.domain.*;
import com.toyproject.sh.dto.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SearchServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PostService postService;

    @Autowired
    SearchService searchService;

    @BeforeEach
    public void set() {
        Member member = new Member();
        memberService.join(member);

        Post post = new Post(member, "AA", "content", Category.FREE);
        postService.createPost(post, "#스키");


    }

    @Test
    public void searchTest() {

        Page<PostResponse> postResponses = searchService.searchPost(PageRequest.of(0, 3), "#스키");
        System.out.println("postResponses = " + postResponses.getContent());
    }

}