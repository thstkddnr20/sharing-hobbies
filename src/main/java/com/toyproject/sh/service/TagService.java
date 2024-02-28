package com.toyproject.sh.service;

import com.toyproject.sh.domain.Post;
import com.toyproject.sh.repository.TagManagerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class TagService {

    private final TagManagerRepository tmRepository;

    public String findTagWithPost(Post post) {
        return tmRepository.findTagWithPost(post);
    }
}
