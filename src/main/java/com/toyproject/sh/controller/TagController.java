package com.toyproject.sh.controller;

import com.toyproject.sh.argumentResolver.Login;
import com.toyproject.sh.domain.Member;
import com.toyproject.sh.domain.TagManager;
import com.toyproject.sh.dto.GenerateMemberTagForm;
import com.toyproject.sh.exception.ExceptionHandler;
import com.toyproject.sh.service.MemberService;
import com.toyproject.sh.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/myTag")
public class TagController {

    private final TagService tagService;
    private final MemberService memberService;

    @GetMapping("/generateTag")
    public String generateTagForm(@Login Member loginMember,
                                  Model model) {
        model.addAttribute("generateMemberTagForm", new GenerateMemberTagForm());
        return "tag/generate";
    }
    @PostMapping("/generateTag")
    public String generateTag(@Login Member loginmember,
                              @ModelAttribute GenerateMemberTagForm generateMemberTagForm,
                              BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "tag/generate";
        }

        try {
            memberService.addMemberTag(generateMemberTagForm.getTagName(), loginmember);
        } catch (ExceptionHandler e) {
            bindingResult.rejectValue("tagName", "TagAlreadyOrNotStartWithSharp");
            return "tag/generate";
        }
        return "redirect:/myPage/";
    }
}
