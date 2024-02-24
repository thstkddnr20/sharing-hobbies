package com.toyproject.sh.controller;

import com.toyproject.sh.argumentResolver.Login;
import com.toyproject.sh.domain.Member;
import com.toyproject.sh.dto.SearchFriendForm;
import com.toyproject.sh.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/friends") // 친구 검색, 친구 추가, 친구 목록, 친구 수락
public class FriendController {

    private final MemberService memberService;

    @GetMapping("/")
    public String friendForm(Model model) {
        model.addAttribute("friendForm", new SearchFriendForm()); // 검색에 대한 이메일 폼
        return "friends/search";
    }

    @PostMapping("/")
    public String friend(@Login Member loginMember,
                         @ModelAttribute("friendForm") @Validated SearchFriendForm friendForm,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "friends/search";
        }

        Member member = memberService.findMember(friendForm.getEmail());

        if (member == null) {
            bindingResult.rejectValue("foundEmail", "NotFound");
            return "friends/search";
        }

        if (member.getEmail().equals(loginMember.getEmail())) {
            bindingResult.rejectValue("foundEmail", "FoundMyself");
            return "friends/search";
        }

        friendForm.setFoundEmail(member.getEmail());
        return "friends/search";
    }

    @GetMapping("/addFriend/{email}")
    public String addFriend(@PathVariable String email,
                            @Login Member loginMember) {

        Member member = memberService.findMember(email);
        memberService.requestFriend(loginMember, member);
        return "redirect:/myPage/";
    }

    @GetMapping("/acceptFriend/{email}")
    public String acceptFriend(@PathVariable String email,
                               @Login Member loginMember) {

        Member member = memberService.findMember(email);
        memberService.acceptFriend(loginMember, member);
        return "redirect:/myPage/";
    }

    @GetMapping("/denyFriend/{email}")
    public String denyFriend(@PathVariable String email,
                             @Login Member loginMember) {
        Member member = memberService.findMember(email);
        memberService.denyFriend(loginMember, member);
        return "redirect:/myPage/";
    }
}
