package com.toyproject.sh.service;

import com.toyproject.sh.domain.*;
import com.toyproject.sh.dto.PostAndTagNameDto;
import com.toyproject.sh.dto.PostResponse;
import com.toyproject.sh.exception.ExceptionHandler;
import com.toyproject.sh.repository.PostRepository;
import com.toyproject.sh.repository.TagManagerRepository;
import com.toyproject.sh.repository.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final TagManagerRepository tmRepository;

    public void createPost(Post post, String tagName){
        Long maxCount = postRepository.findMaxCount();
        if (maxCount == null) {
            post.setCount(1L);
        }
        else {
            post.setCount(maxCount+1L);
        }
        postRepository.save(post);
        if (!(tagName.isEmpty())){
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

    public void updatePost(Post post, String tagName) {
        postRepository.save(post); // 1.태그가 없었는데 생길경우, 2.태그가 있었는데 바뀔경우, 3.태그가 있었는데 없어질 경우 4.없는데 그대로 없는경우
        Optional<TagManager> tmByPost = tmRepository.findTMByPost(post);
        if (tagName.isEmpty() && tmByPost.isPresent()) {
            tmRepository.delete(tmByPost.get());
        }
        else if (tagName.isEmpty()) {
        }
        else if (tmByPost.isEmpty()){
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
        else {
            validateTagName(tagName);
            Optional<Tag> byName = tagRepository.findByName(tagName);
            if (byName.isEmpty()) {
                Tag tag = new Tag(tagName);
                tagRepository.save(tag);
                TagManager tagManager = tmByPost.get();
                tagManager.setTag(tag);
                tmRepository.save(tagManager);
            }
            else {
                Tag tag = byName.get();
                TagManager tagManager = tmByPost.get();
                tagManager.setTag(tag);
                tmRepository.save(tagManager);
            }
        }
    }

    private void validateTagName(String tagName) {
        if (!tagName.startsWith("#")) {
            throw new ExceptionHandler.TagNotStartWithSharpException();
        }
    }

    public Page<Post> findOnesPost(Member member, Pageable pageable) { //TODO Pageable 인자 전달하는 것 컨트롤러 부분에서 테스트 필요
        Page<Post> posts = postRepository.findAllByMember(member, pageable);
        if (!posts.isEmpty()) {
            return posts;
        }
        else {
            throw new ExceptionHandler.PostNotFoundException();
        }
    }

    public Page<PostResponse> findAllPost(Pageable pageable) {
        int page = pageable.getPageNumber() - 1; // page 위치에 있는 값은 0부터 시작한다.
        int pageLimit = 3; // 한페이지에 보여줄 글 개수

        // 한 페이지당 10개식 글을 보여주고 정렬 기준은 ID기준으로 내림차순
        Page<Post> posts = postRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        return posts.map(post -> new PostResponse(post));
    }

    public Post findSinglePost(Long id) {
        return postRepository.findPostWithComments(id);
    }

    public PostAndTagNameDto findSinglePostWithTag(Long id) { // 1번째 인자 Post, 두번째 인자 tagName
        return postRepository.findPostAndTagName(id);
    }

    public Page<Post> searchAll(Pageable pageable, String search) {
        int page = pageable.getPageNumber() - 1; // page 위치에 있는 값은 0부터 시작한다.
        int pageLimit = 3; // 한페이지에 보여줄 글 개수

        if (search.startsWith("#")) {
            Page<Post> post = tmRepository.findPostByTagName(search, PageRequest.of(page, pageLimit));
            if (post.isEmpty()) {
                throw new ExceptionHandler.PostNotFoundException();
            }
            return post;
        }
        else {
            String nameSearch = "%" + search + "%";
            Page<Post> post = postRepository.findPostByThumbnail(nameSearch, PageRequest.of(page, pageLimit));
            if (post.isEmpty()) {
                throw new ExceptionHandler.PostNotFoundException();
            }
            return post;
        }
    }

    public String findPostAuthor(Long postId) {
        return postRepository.findPostAuthor(postId);
    }

    public Post findOnePost(Long postId) {
        return postRepository.findOnePost(postId);
    }
}
