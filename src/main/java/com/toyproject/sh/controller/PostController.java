package com.toyproject.sh.controller;

import com.toyproject.sh.domain.Category;
import com.toyproject.sh.domain.Member;
import com.toyproject.sh.domain.Post;
import com.toyproject.sh.dto.*;
import com.toyproject.sh.exception.ExceptionHandler;
import com.toyproject.sh.service.PostService;
import com.toyproject.sh.session.SessionConst;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
        //TODO 게시글 생성중 오류가 나면 만들어진 객체에 이미 ID가 할당되어 게시글 번호가 펌핑하게 됨

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

    @GetMapping("/paging") //query parameter로 size와 page정보를 넘겨주면 해당 페이지의 게시글들을 보여준다.
    public String findAllPosts(@PageableDefault(page = 1) Pageable pageable, Model model) {
        Page<PostResponse> allPost = postService.findAllPost(pageable);

        int blockLimit = 10; //page 개수 설정
        int startPage = (((int) Math.ceil(((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
        int endPage = Math.min((startPage + blockLimit - 1), allPost.getTotalPages());

        model.addAttribute("postResponse", allPost);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "posts/paging";
    }

    @GetMapping("/{postId}")
    public String searchSinglePost(@PathVariable Long postId,
                                   Model model,
                                   @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) {
        // SessionAttribute로 가져온 Member의 이메일과 게시글의 작성자가 같을 경우 수정버튼 활성화
        Post singlePost = postService.findSinglePost(postId);
        if (singlePost == null) {
            log.error("게시글 없음 오류");
            throw new ExceptionHandler.PostNotFoundException();
        }

        List<CommentResponse> commentResponse = singlePost.getComments().stream()
                .map(comment -> new CommentResponse(comment))
                .toList();

        PostCommentsResponse postCommentsResponse = new PostCommentsResponse(singlePost, commentResponse);

        model.addAttribute("singlePost", postCommentsResponse);
        model.addAttribute("loginMember", loginMember);
        return "posts/single";
    }

    @GetMapping("/{postId}/edit")
    public String editForm(@PathVariable Long postId,
                           Model model,
                           @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) {
        PostAndTagNameDto singlePostWithTag = postService.findSinglePostWithTag(postId);

        if (loginMember != null && loginMember.getEmail().equals(singlePostWithTag.getPost().getMember().getEmail())) {
            FormCreatePostRequest postRequest = new FormCreatePostRequest(singlePostWithTag.getPost(), singlePostWithTag.getTagName());
            model.addAttribute("postRequest", postRequest);
            model.addAttribute("postId", postId);
            return "posts/edit";
        }
        else {
            return "redirect:/";
        }

    }

    @PostMapping("/{postId}/edit") //TODO 수정 되는것 확인, NonUniqueResultException 발생 수정필요
    public String edit(@Valid FormCreatePostRequest postRequest,
                       BindingResult bindingResult,
                       @PathVariable Long postId) {
        Post singlePost = postService.findSinglePost(postId);
        singlePost.setCategory(Category.from(postRequest.getCategory()));
        singlePost.setThumbnail(postRequest.getThumbnail());
        singlePost.setContent(postRequest.getContent());

        postService.updatePost(singlePost, postRequest.getTagName());
        return "redirect:/posts/{postId}";
    }
}
