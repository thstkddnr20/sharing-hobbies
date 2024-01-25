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
import java.util.Optional;

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
        Optional<Member> byEmail = memberRepository.findByEmail(member.getEmail());
        if (byEmail.isPresent()) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }
    }

    public List<Member> findFriends(Member member) {
        Optional<List<Member>> findAll = friendRepository.findAllMyFriends(member, FriendStatus.FRIEND);
        return findAll.orElseThrow(()-> new IllegalStateException("친구가 없습니다."));
    }

    public void requestFriend(Member member, Member friend) {
        //이미 요청이있는지 체크, 이미 친구인치 체크 (테스트완료)
        Optional<Friend> findOne = friendRepository.findOneByMemberAndFriend(member, friend);
        if (findOne.isPresent()){
            if (findOne.get().getFriendStatus() == FriendStatus.FRIEND){
                throw new IllegalStateException("이미 친구입니다.");
            }
            throw new IllegalStateException("이미 친구요청을 했습니다.");
        }
        else {
            List<Friend> created = Friend.requestAdd(member, friend);
            friendRepository.save(created.get(0));
            friendRepository.save(created.get(1));
        }
    }

    public void acceptFriend(Member member, Member friend) {
        Optional<Friend> firstOne = friendRepository.findOneByMemberAndFriend(member, friend);
        if (firstOne.isPresent()){
            Friend getFirst = firstOne.get();
            if (getFirst.getFriendStatus() == FriendStatus.WAITING){
                getFirst.setFriendStatus(FriendStatus.FRIEND);
                Friend getSecond = friendRepository.findOneByMemberAndFriend(friend, member).get();
                getSecond.setFriendStatus(FriendStatus.FRIEND);
            }
            else {
                throw new IllegalStateException("친구요청 대기중이 아닙니다.");
            }
        }
        else {
            throw new IllegalStateException("요청이 없습니다.");
        }
    }
}
