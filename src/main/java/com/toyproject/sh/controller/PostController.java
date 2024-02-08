package com.toyproject.sh.controller;

import com.toyproject.sh.domain.Category;
import com.toyproject.sh.domain.Member;
import com.toyproject.sh.domain.Post;
import com.toyproject.sh.dto.FormCreatePostRequest;
import com.toyproject.sh.exception.ExceptionHandler;
import com.toyproject.sh.service.PostService;
import com.toyproject.sh.session.SessionConst;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @GetMapping("/new")
    public String newPostForm(Model model) {
        model.addAttribute("postRequest", new FormCreatePostRequest());
        return "posts/newPost";
    }


    @PostMapping("/new")
    public String createPost(@Valid FormCreatePostRequest postRequest,
                             BindingResult bindingResult,
                             @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                             Model model) {
        if (loginMember == null) {
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            log.error("게시글 생성 입력 오류 = {}", bindingResult.getFieldErrors());
            model.addAttribute("postRequest", postRequest);
            return "posts/newPost";
        }

        Post post = new Post(loginMember, postRequest.getThumbnail(), postRequest.getContent(), Category.from(postRequest.getCategory()));
        try {
            postService.createPost(post, postRequest.getTagName());
        } catch (ExceptionHandler e) {
            bindingResult.rejectValue("tagName", "TagName must start with #", e.getMessage());
            log.error("게시글 생성 서비스 중 오류 = {}", e.getMessage());
            model.addAttribute("postRequest", postRequest);
            return "posts/newPost";
        }

        return "redirect:/";
    }
}
