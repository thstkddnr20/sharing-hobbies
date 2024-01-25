package com.toyproject.sh.service;

import com.toyproject.sh.domain.Friend;
import com.toyproject.sh.domain.FriendStatus;
import com.toyproject.sh.domain.Member;
import com.toyproject.sh.repository.FriendRepository;
import com.toyproject.sh.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;

    public Long join(Member member) { // 회원가입
        validateDuplicateMember(member); // 중복 이메일 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        Member byEmail = memberRepository.findByEmail(member.getEmail());
        if (byEmail != null) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }
    }

    public List<Member> findFriends(Member member) {
        return friendRepository.findAllMyFriends(member, FriendStatus.FRIEND);
    }

    public void requestFriend(Member member, Member friend) {
        Friend friends = new Friend();
    }
}
