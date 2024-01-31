package com.toyproject.sh.controller;

import com.toyproject.sh.session.SessionConst;
import com.toyproject.sh.domain.Member;
import com.toyproject.sh.dto.MemberRequest;
import com.toyproject.sh.exception.ExceptionHandler;
import com.toyproject.sh.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/new") // 회원가입 에러처리 완료
    public ResponseEntity<String> saveMember(@RequestBody @Valid MemberRequest request, BindingResult bindingResult) {

        if (bindingResult.hasErrors()){
            StringBuilder errorMessage = new StringBuilder("Validation failed for the following fields: ");
            bindingResult.getFieldErrors().forEach(error ->
                    errorMessage.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append("; ")
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage.toString());
        }

        Member member = new Member(request.getEmail(), passwordEncoder.encode(request.getPassword()));
        try {
            Long id = memberService.join(member);
            return ResponseEntity.created(URI.create("/members/" + id)).build();
        } catch (ExceptionHandler e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid MemberRequest memberRequest, BindingResult bindingResult, HttpServletRequest request){

        if (bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body("잘못된 입력입니다.");
        }

        Member member = memberService.findMember(memberRequest.getEmail());

        if (member == null || !passwordEncoder.matches(memberRequest.getPassword(), member.getPassword())) {
            return ResponseEntity.badRequest().body("아이디 또는 비밀번호가 잘못되었습니다.");
        }
        else {
            //getSession 세션이 있는경우 세션반환, 없을경우 새로운 세션 생성
            HttpSession session = request.getSession();
            //세션에 로그인 회원 정보 저장
            session.setAttribute(SessionConst.LOGIN_MEMBER, member);

            return ResponseEntity.ok("로그인 완료");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok("로그아웃 완료");
    }
}
