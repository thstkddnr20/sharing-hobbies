package com.toyproject.sh.controller.api;

import com.toyproject.sh.domain.Comment;
import com.toyproject.sh.domain.Member;
import com.toyproject.sh.domain.Post;
import com.toyproject.sh.dto.*;
import com.toyproject.sh.exception.ExceptionHandler;
import com.toyproject.sh.service.CommentService;
import com.toyproject.sh.service.PostService;
import com.toyproject.sh.session.SessionConst;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostApiController {

    private final PostService postService;
    private final CommentService commentService;

    @PostMapping("/new")
    public ResponseEntity<String> createPost(@RequestBody @Valid ApiCreatePostRequest postRequest,
                             BindingResult bindingResult,
                             @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) {
        if (loginMember == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한이 없습니다.");
        }
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Validation failed for the following fields: ");
            bindingResult.getFieldErrors().forEach(error ->
                    errorMessage.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append("; ")
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage.toString());
        }

        Post post = new Post(loginMember, postRequest.getThumbnail(), postRequest.getContent(), postRequest.getCategory());
        try {
            postService.createPost(post, postRequest.getTagName());
        } catch (ExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(post.getId().toString());
    }

    @GetMapping("/all") //query parameter로 size와 page정보를 넘겨주면 해당 페이지의 게시글들을 보여준다.
    public List<PostResponse> findAllPosts(@PageableDefault Pageable pageable) {
        Page<Post> allPost = postService.findAllPost(pageable);
        return allPost.stream()
                .map(post -> new PostResponse(post))
                .toList();
    }

    @PostMapping("/search")
    public List<PostResponse> searchPostOrTag(@RequestBody SearchPostRequest request, @PageableDefault Pageable pageable) {
        Page<Post> findPost = postService.searchAll(pageable, request.getSearch());
        return findPost.stream()
                .map(post -> new PostResponse(post))
                .toList();
    }

    @GetMapping("/{postId}")
    public PostCommentsResponse searchSinglePost(@PathVariable Long postId) {
        Post singlePost = postService.findSinglePost(postId);
        if (singlePost == null) {
            throw new ExceptionHandler.PostNotFoundException();
        }

        List<CommentResponse> commentResponse = singlePost.getComments().stream()
                .map(comment -> new CommentResponse(comment))
                .toList();

        return new PostCommentsResponse(singlePost, commentResponse);
    }

    @PostMapping("/{postId}")
    public String createComment(@PathVariable Long postId,
                                @RequestBody CreateCommentRequest commentRequest,
                                @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) {
        if (loginMember == null) {
            return "redirect:/members/login";
        }
        Post singlePost = postService.findSinglePost(postId);
        Comment comment = new Comment(loginMember, singlePost, commentRequest.getContent());
        commentService.saveComment(comment);
        return "/post/{postId}";
    }
}
