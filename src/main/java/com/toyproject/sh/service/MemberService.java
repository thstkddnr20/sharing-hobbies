package com.toyproject.sh.service;

import com.toyproject.sh.domain.Member;
import com.toyproject.sh.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        Member byEmail = memberRepository.findByEmail(member.getEmail());
        if (byEmail != null) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }
    }
}
