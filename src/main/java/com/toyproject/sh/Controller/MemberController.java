package com.toyproject.sh.Controller;

import com.toyproject.sh.domain.Member;
import com.toyproject.sh.dto.CreateMemberRequest;
import com.toyproject.sh.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/new") // 회원가입
    public ResponseEntity<String> saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {

        Member member = new Member(request.getEmail(), request.getPassword());
        Long id = memberService.join(member);

        return ResponseEntity.created(URI.create("/members/" + id)).build();
    }
}
