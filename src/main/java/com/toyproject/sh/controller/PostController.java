package com.toyproject.sh.controller;

import com.toyproject.sh.domain.Category;
import com.toyproject.sh.domain.Member;
import com.toyproject.sh.domain.Post;
import com.toyproject.sh.dto.*;
import com.toyproject.sh.exception.ExceptionHandler;
import com.toyproject.sh.service.PostService;
import com.toyproject.sh.session.SessionConst;
import jakarta.servlet.http.HttpSession;
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

    @GetMapping("/new")
    public String newPostForm(Model model) {
        model.addAttribute("postRequest", new CreatePostForm());
        return "posts/newPost";
    }


    @PostMapping("/new")
    public String createPost(@ModelAttribute("postRequest") @Validated CreatePostForm postRequest,
                             BindingResult bindingResult,
                             @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                             HttpSession session) {

        if (loginMember == null) {
            return "redirect:/";
        }

        if (bindingResult.hasErrors()) {
            log.error("게시글 생성 입력 오류 = {}", bindingResult.getFieldErrors());
            return "posts/newPost";
        }

        if (session.getAttribute("post") == null) {
            Post post = new Post(loginMember, postRequest.getThumbnail(), postRequest.getContent(), postRequest.getCategory());

            try {
                postService.createPost(post, postRequest.getTagName());
            } catch (ExceptionHandler e) {
                log.info("id={}", post.getId());
                bindingResult.rejectValue("tagName", "TagNotStartWithSharp");
                log.error("게시글 생성 서비스 중 오류 = {}", bindingResult.getFieldErrors());
                session.setAttribute("post", post);
                return "posts/newPost";
            }
        }
        else {
            Post post = (Post) session.getAttribute("post");
            post.setThumbnail(postRequest.getThumbnail());
            post.setContent(postRequest.getContent());
            post.setCategory(postRequest.getCategory());
            try {
                postService.createPost(post, postRequest.getTagName());
            } catch (ExceptionHandler e) {
                log.info("id={}", post.getId());
                bindingResult.rejectValue("tagName", "TagNotStartWithSharp");
                log.error("게시글 생성 서비스 중 오류 = {}", bindingResult.getFieldErrors());
                return "posts/newPost";
            }
        }
        //TODO 게시글 생성중 오류가 나면 만들어진 객체에 이미 ID가 할당되어 게시글 번호가 펌핑하게 됨
        //결과 : 서비스 계층을 거쳐가면 ID가 자동으로 할당되어 게시글번호가 증가해서 나온다....
        //해결방안 : 애매하다. 게시글 번호로 게시글을 찾으려면 결국 DB에서 관리해야하는거 아닌가? 게시글 번호를 뭐 페이징 기능같은걸로
        // 가져온다고 생각해도 그 번호로 게시글을 못찾잖아
        // 찾은 결론 : DB에 저장된 실제 PK값은 보여주지말고, 따로 게시글의 번호를 채번하라..


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
            CreatePostForm postRequest = new CreatePostForm(singlePostWithTag.getPost(), singlePostWithTag.getTagName());
            model.addAttribute("postRequest", postRequest);
            model.addAttribute("postId", postId);
            return "posts/edit";
        } else {
            return "redirect:/";
        }

    }

    @PostMapping("/{postId}/edit") //TODO 수정 되는것 확인, NonUniqueResultException 발생 수정필요
    public String edit(@Valid CreatePostForm postRequest,
                       BindingResult bindingResult,
                       @PathVariable Long postId) {
        Post singlePost = postService.findSinglePost(postId);
//        singlePost.setCategory(Category.from(postRequest.getCategory()));
        singlePost.setThumbnail(postRequest.getThumbnail());
        singlePost.setContent(postRequest.getContent());

        postService.updatePost(singlePost, postRequest.getTagName());
        return "redirect:/posts/{postId}";
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
