package com.toyproject.sh.service;

import com.toyproject.sh.domain.Post;
import com.toyproject.sh.domain.Tag;
import com.toyproject.sh.domain.TagManager;
import com.toyproject.sh.repository.PostRepository;
import com.toyproject.sh.repository.TagManagerRepository;
import com.toyproject.sh.repository.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final TagManagerRepository tmRepository;

    public void createPost(Post post, String tagName){
        postRepository.save(post);
        if (!(tagName == null)){
            validateTagName(tagName);
            Optional<Tag> byName = tagRepository.findByName(tagName);
            if (byName.isEmpty()) {
                Tag tag = new Tag(tagName);
                tagRepository.save(tag);
                TagManager tagManager = new TagManager(tag, post);
                tmRepository.save(tagManager);
            }
            else {
                Tag tag = byName.get();
                TagManager tagManager = new TagManager(tag, post);
                tmRepository.save(tagManager);
            }
        }
    }

    private void validateTagName(String tagName) {
        if (!tagName.startsWith("#")) {
            throw new IllegalStateException("태그가 #으로 시작하지 않습니다.");
        }
    }
}
