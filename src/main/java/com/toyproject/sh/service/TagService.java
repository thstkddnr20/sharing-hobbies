package com.toyproject.sh.service;

import com.toyproject.sh.domain.Post;
import com.toyproject.sh.dto.MemberTagForm;
import com.toyproject.sh.repository.TagManagerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TagService {

    private final TagManagerRepository tmRepository;

    public String findTagWithPost(Post post) {
        return tmRepository.findTagWithPost(post);
    }

    public MemberTagForm findTagWithMember(String email) {
        List<String> tagWithMember = tmRepository.findTagWithMember(email);
        return new MemberTagForm(tagWithMember); // list로 tag가 담긴 Dto 그대로 반환
    }
}
