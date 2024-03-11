package com.toyproject.sh.controller;

import com.toyproject.sh.domain.Member;
import com.toyproject.sh.dto.MemberRequest;
import com.toyproject.sh.exception.ExceptionHandler;
import com.toyproject.sh.service.MemberService;
import com.toyproject.sh.session.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
public class LoginController {

    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;

    @GetMapping("/new")
    public String createUserForm(Model model) {
        model.addAttribute("memberRequest", new MemberRequest());
        return "login/register";
    }

    @PostMapping("/new") //Query 2번
    public String register(@ModelAttribute @Valid MemberRequest request, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder("Validation failed for the following fields: ");
            bindingResult.getFieldErrors().forEach(error ->
                    errorMessage.append(error.getField()).append(" - ").append(error.getDefaultMessage())
            );
            log.error("회원가입 오류={}", errorMessage);
            return "login/register";
        }

        try {
            Member member = new Member(request.getEmail(), passwordEncoder.encode(request.getPassword()));
            Long id = memberService.join(member);
            return "redirect:/";
        } catch (ExceptionHandler e) {
            bindingResult.rejectValue("email", "Duplicate");
            log.error("bindingResult={}", bindingResult.getFieldErrors());
            log.error("회원가입 오류={}", e.getMessage());
            return "login/register";
        }
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("memberRequest", new MemberRequest());
        return "login/login";
    }

    @PostMapping("/login") // Query 1번
    public String login(@ModelAttribute @Valid MemberRequest memberRequest,
                        BindingResult bindingResult,
                        @RequestParam(defaultValue = "/") String redirectURL,
                        HttpServletRequest request,
                        HttpServletResponse response){ // redirectURL이 안넘어옴..

        if (bindingResult.hasErrors()){
            StringBuilder errorMessage = new StringBuilder("Validation failed for the following fields: ");
            bindingResult.getFieldErrors().forEach(error ->
                    errorMessage.append(error.getField()).append(" - ").append(error.getDefaultMessage())
            );
            log.error("로그인 오류={}", errorMessage);
            return "login/login";
        }

        Member member = memberService.findMember(memberRequest.getEmail());

        if (member == null || !passwordEncoder.matches(memberRequest.getPassword(), member.getPassword())) {
            bindingResult.rejectValue("password", "NotFound");
            log.error("bindingResult={}", bindingResult.getFieldErrors());
            log.error("로그인 정보 입력 오류");
            return "login/login";
        }



        log.info("redirect = {}", redirectURL);
        //getSession 세션이 있는경우 세션반환, 없을경우 새로운 세션 생성
        HttpSession session = request.getSession();
        //세션에 로그인 회원 정보 저장
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);

        return "redirect:" + redirectURL;

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
