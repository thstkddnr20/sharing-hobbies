package com.toyproject.sh.controller;

import com.toyproject.sh.argumentResolver.Login;
import com.toyproject.sh.domain.Member;
import com.toyproject.sh.dto.RequestFriendForm;
import com.toyproject.sh.service.MemberService;
import com.toyproject.sh.service.PostService;
import com.toyproject.sh.session.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/myPage")
public class MyPageController {

    private final MemberService memberService;
    private final PostService postService;

    @GetMapping("/") //마이페이지 기본페이지 // 친구 요청중인 폼, 친구 목록 폼, 친구 요청 대기 폼
    public String basicMyPage(@Login Member loginMember,
                              Model model){
        if (loginMember == null) {
            return "redirect:/";
        }
        else {
            model.addAttribute("email", loginMember.getEmail());
            model.addAttribute("requestFriendForm", memberService.findRequestFriends(loginMember.getEmail()));
            model.addAttribute("waitingFriendForm", memberService.findWaitingFriends(loginMember.getEmail()));
            return "myPage/basic";
        }
    }

    

}

