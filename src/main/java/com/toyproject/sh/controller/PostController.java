package com.toyproject.sh.controller;

import com.toyproject.sh.argumentResolver.Login;
import com.toyproject.sh.domain.Category;
import com.toyproject.sh.domain.Comment;
import com.toyproject.sh.domain.Member;
import com.toyproject.sh.domain.Post;
import com.toyproject.sh.dto.*;
import com.toyproject.sh.exception.ExceptionHandler;
import com.toyproject.sh.service.CommentService;
import com.toyproject.sh.service.PostService;
import com.toyproject.sh.service.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final TagService tagService;

    @GetMapping("/new")
    public String newPostForm(Model model) {
        model.addAttribute("postRequest", new CreatePostForm());
        return "posts/newPost";
    }


    @PostMapping("/new") // Query 태그없을경우 2번 , 태그있을경우 5개..
    public String createPost(@ModelAttribute("postRequest") @Validated CreatePostForm postRequest,
                             BindingResult bindingResult,
                             @Login Member loginMember) {

        if (bindingResult.hasErrors()) {
            log.error("게시글 생성 입력 오류 = {}", bindingResult.getFieldErrors());
            return "posts/newPost";
        }

        Post post = new Post(loginMember, postRequest.getThumbnail(), postRequest.getContent(), postRequest.getCategory());

        try {
            postService.createPost(post, postRequest.getTagName());
        } catch (ExceptionHandler e) {
            log.info("id={}", post.getId());
            bindingResult.rejectValue("tagName", "TagNotStartWithSharp");
            log.error("게시글 생성 서비스 중 오류 = {}", bindingResult.getFieldErrors());
            return "posts/newPost";
        }

        return "redirect:/";
    }

    @GetMapping("/paging") //query parameter로 size와 page정보를 넘겨주면 해당 페이지의 게시글들을 보여준다. // Query 2개
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

    @GetMapping("/{postId}") //댓글작성: 댓글작성에 대한 폼 전달-> html에서 댓글 등록 -> PostMapping으로 넘기기  댓글 삭제 :html에서 댓글 삭제 버튼 생성(로그인멤버와 댓글작성자가 같은지 확인필요) -> 댓글삭제버튼 생성
    public String singlePostAndCommentForm(@PathVariable Long postId, // Query 2개 -> 3개
                             Model model,
                             @Login Member loginMember,
                             CreateCommentForm commentForm) {
        // SessionAttribute로 가져온 Member의 이메일과 게시글의 작성자가 같을 경우 수정버튼 활성화
        Post singlePost = postService.findSinglePost(postId);
        if (singlePost == null) {
            log.error("게시글 없음 오류");
            throw new ExceptionHandler.PostNotFoundException();
        }

        List<CommentResponse> commentResponse = singlePost.getComments().stream()
                .map(comment -> new CommentResponse(comment))
                .toList();

        String tagName = tagService.findTagWithPost(singlePost);
        PostCommentsResponse postCommentsResponse = new PostCommentsResponse(singlePost, commentResponse, tagName); // 태그이름도 추가

        commentForm.setEmail(loginMember.getEmail());
        model.addAttribute("singlePost", postCommentsResponse);
        model.addAttribute("loginMember", loginMember);
        model.addAttribute("commentForm", commentForm);
        return "posts/single";
    }

    @PostMapping("/{postId}")
    public String createComment(@Validated @ModelAttribute("commentForm") CreateCommentForm commentForm, // Query 댓글만들기 2개
                                BindingResult bindingResult,
                                @PathVariable Long postId,
                                @Login Member loginMember) {

        if (bindingResult.hasErrors()) {
            log.error("댓글 생성중 오류 = {}", bindingResult);
            return "posts/single";
        }

        if (loginMember != null && loginMember.getEmail().equals(commentForm.getEmail())) {
            Post post = postService.findOnePost(postId);
            Comment comment = new Comment(loginMember, post, commentForm.getContent());
            post.getComments().add(comment); // 포스트에도 댓글 넣어주기
            log.info("getComment={}", post.getComments());
            commentService.saveComment(comment);
            return "redirect:/posts/{postId}";
        }

        return "redirect:/";
    }

    @GetMapping("/{postId}/edit") // Query 불러오기 2개
    public String editForm(@PathVariable Long postId,
                           Model model,
                           @Login Member loginMember) {
        PostAndTagNameDto singlePostWithTag = postService.findSinglePostWithTag(postId);

        if (loginMember != null && loginMember.getEmail().equals(singlePostWithTag.getPost().getMember().getEmail())) {
            UpdatePostForm postRequest = new UpdatePostForm(singlePostWithTag.getPost(), singlePostWithTag.getTagName());
            model.addAttribute("postRequest", postRequest);
            return "posts/edit";
        } else {
            return "redirect:/";
        }

    }

    @PostMapping("/{postId}/edit") // Query 수정 7개.....
    public String edit(@Valid @ModelAttribute("postRequest") UpdatePostForm postRequest,
                       BindingResult bindingResult,
                       @PathVariable Long postId,
                       @Login Member loginMember) {

        if (bindingResult.hasErrors()) {
            log.error("게시글 수정 중 오류 = {}", bindingResult.getFieldErrors());
            return "posts/edit";
        }

        Post post = postService.findOnePost(postId);

        if (loginMember != null && post.getMember().getEmail().equals(loginMember.getEmail())) { // 보안 문제 어떻게 해결?

            post.setCategory(postRequest.getCategory());
            post.setThumbnail(postRequest.getThumbnail());
            post.setContent(postRequest.getContent());
            postService.updatePost(post, postRequest.getTagName());
            return "redirect:/posts/{postId}";
        }
        else {
            return "redirect:/";
        }
    }
    @GetMapping("/{postId}/delete")
    public String delete(@PathVariable Long postId,
                         @Login Member loginMember) {

        Post post = postService.findOnePost(postId);
        if (loginMember != null && loginMember.getEmail().equals(post.getMember().getEmail())) {
            postService.deletePost(post);
            return "redirect:/posts/paging";
        } else {
            return "redirect:/";
        }
    }

    @ModelAttribute("categorys")
    public List<Categorys> populateCategory(){
        List<Categorys> categorys = new ArrayList<>();
        for (Category value : Category.values()) {
            categorys.add(new Categorys(value.name(), value.getValue()));
        }
        return categorys;
    }
}
