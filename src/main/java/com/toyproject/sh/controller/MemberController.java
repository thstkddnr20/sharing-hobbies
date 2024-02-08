package com.toyproject.sh.controller;

import com.toyproject.sh.domain.Member;
import com.toyproject.sh.dto.MemberRequest;
import com.toyproject.sh.exception.ExceptionHandler;
import com.toyproject.sh.service.MemberService;
import com.toyproject.sh.session.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/new")
    public String createUserForm(Model model) {
        model.addAttribute("memberRequest", new MemberRequest());
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
            return "redirect:/";
        } catch (ExceptionHandler e) {
            bindingResult.rejectValue("email", "Duplicate Email", e.getMessage());
            log.error("회원가입 오류={}", e.getMessage());
            return "members/register";
        }
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("memberRequest", new MemberRequest());
        return "members/login";
    }

    @PostMapping("/login")
    public String login(@Valid MemberRequest memberRequest, BindingResult bindingResult, HttpServletRequest request){

        if (bindingResult.hasErrors()){
            StringBuilder errorMessage = new StringBuilder("Validation failed for the following fields: ");
            bindingResult.getFieldErrors().forEach(error ->
                    errorMessage.append(error.getField()).append(" - ").append(error.getDefaultMessage())
            );
            log.error("로그인 오류={}", errorMessage);
            return "members/login";
        }

        Member member = memberService.findMember(memberRequest.getEmail());

        if (member == null || !passwordEncoder.matches(memberRequest.getPassword(), member.getPassword())) {
            bindingResult.rejectValue("email", "Duplicate Email", "아이디 또는 비밀번호가 잘못되었습니다.");
            log.error("로그인 정보 입력 오류");
            return "members/login";
        }
        else {
            //getSession 세션이 있는경우 세션반환, 없을경우 새로운 세션 생성
            HttpSession session = request.getSession();
            //세션에 로그인 회원 정보 저장
            session.setAttribute(SessionConst.LOGIN_MEMBER, member);

            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
}
