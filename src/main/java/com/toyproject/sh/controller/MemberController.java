package com.toyproject.sh.controller;

import com.toyproject.sh.domain.Member;
import com.toyproject.sh.dto.MemberRequest;
import com.toyproject.sh.exception.ExceptionHandler;
import com.toyproject.sh.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/members")
public class MemberController {

    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;

    @GetMapping("/newForm")
    public String createUserForm(MemberRequest memberRequest) {
        return "members/register";
    }

    @PostMapping("/new")
    public String register(@Valid MemberRequest request, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Validation failed for the following fields: ");
            bindingResult.getFieldErrors().forEach(error ->
                    errorMessage.append(error.getField()).append(" - ").append(error.getDefaultMessage())
            );
            log.error("회원가입 오류={}", errorMessage);
            return "members/register";
        }

        try {
            Member member = new Member(request.getEmail(), passwordEncoder.encode(request.getPassword()));
            Long id = memberService.join(member);
            return "redirect:/index";
        } catch (ExceptionHandler e) {
            bindingResult.rejectValue("email", "Duplicate Email", e.getMessage());
            log.error("회원가입 오류={}", e.getMessage());
            return "members/register";
        }
    }
}
