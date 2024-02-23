package com.toyproject.sh.service;

import com.toyproject.sh.domain.*;
import com.toyproject.sh.exception.ExceptionHandler;
import com.toyproject.sh.repository.FriendRepository;
import com.toyproject.sh.repository.MemberRepository;
import com.toyproject.sh.repository.TagManagerRepository;
import com.toyproject.sh.repository.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;
    private final TagManagerRepository tmRepository;
    private final TagRepository tagRepository;


    public Long join(Member member) { // 회원가입
        validateDuplicateMember(member); // 중복 이메일 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        Optional<Member> byEmail = memberRepository.findByEmail(member.getEmail());
        if (byEmail.isPresent()) {
            throw new ExceptionHandler.DuplicateEmailException();
        }
    }

    public Member findMember(String email) {
        return memberRepository.findMemberByEmail(email);
    }

    /**
     * 친구관련
     */
    public Page<Member> findFriends(Member member, Pageable pageable) {
        Page<Member> findAll = friendRepository.findAllMyFriends(member, FriendStatus.FRIEND, pageable);
        if (!findAll.isEmpty()) {
            return findAll;
        }
        else {
            throw new ExceptionHandler.FriendException("친구가 없습니다.");
        }
    }

    public void requestFriend(Member member, Member friend) {
        //이미 요청이있는지 체크, 이미 친구인치 체크 (테스트완료)
        Optional<Friend> findOne = friendRepository.findOneByMemberAndFriend(member, friend);
        if (findOne.isPresent()){
            if (findOne.get().getFriendStatus() == FriendStatus.FRIEND){
                throw new ExceptionHandler.FriendException("이미 친구입니다.");
            }
            throw new ExceptionHandler.FriendException("이미 친구요청을 했습니다.");
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
                throw new ExceptionHandler.FriendException("친구요청 대기중이 아닙니다.");
            }
        }
        else {
            throw new ExceptionHandler.FriendException("요청이 없습니다.");
        }
    }

    public void denyFriend(Member member, Member friend) {
        Optional<Friend> firstOne = friendRepository.findOneByMemberAndFriend(member, friend);
        if (firstOne.isPresent()){
            Friend getFirst = firstOne.get();
            if (getFirst.getFriendStatus() == FriendStatus.WAITING){
                friendRepository.delete(getFirst);
                Friend getSecond = friendRepository.findOneByMemberAndFriend(friend, member).get();
                friendRepository.delete(getSecond);
            }
            else {
                throw new ExceptionHandler.FriendException("친구요청 대기중이 아닙니다.");
            }
        }
        else {
            throw new ExceptionHandler.FriendException("요청이 없습니다.");
        }
    }

    /**
     * 태그관련
     */
    public void addMemberTag(String tagName, Member member){
        validateTagName(tagName);  // #으로 시작하는지 체크
        validateTagExist(tagName, member); // 이미 해당 멤버가 태그를 가지고있는지 체크
        Optional<Tag> getTag = tagRepository.findByName(tagName);
        if (getTag.isEmpty()) {
            Tag tag = new Tag(tagName);
            tagRepository.save(tag);
            TagManager tagManager = new TagManager(tag, member);
            tmRepository.save(tagManager);
        }
        else {
            Tag tag = getTag.get();
            TagManager tagManager = new TagManager(tag, member);
            tmRepository.save(tagManager);
        }
    }

    public void deleteMemberTag(String tagName, Member member){
        validateTagName(tagName);
        Optional<TagManager> tm = tmRepository.findTMByNameAndMember(tagName, member);
        if (tm.isEmpty()) {
            throw new ExceptionHandler.AllTagException("태그가 없습니다.");
        }
        else {
            TagManager tagManager = tm.get();
            tmRepository.delete(tagManager);
        }
    }

//    public List<String> findTag(Member member) {
//        Optional<List<String>> optionalTag = tmRepository.findTag(member);
//        return optionalTag.orElseThrow(() -> new ExceptionHandler.AllTagException("태그가 없습니다."));
//    }

    private void validateTagExist(String tagName, Member member) {
        Optional<TagManager> tm = tmRepository.findTMByNameAndMember(tagName, member);
        if (tm.isPresent()) {
            throw new ExceptionHandler.AllTagException("이미 태그가 있습니다.");
        }
    }

    private void validateTagName(String tagName) {
        if (!tagName.startsWith("#")) {
            throw new ExceptionHandler.TagNotStartWithSharpException();
        }
    }
}
