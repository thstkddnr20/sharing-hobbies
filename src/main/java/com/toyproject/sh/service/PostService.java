package com.toyproject.sh.service;

import com.toyproject.sh.domain.*;
import com.toyproject.sh.dto.PostResponse;
import com.toyproject.sh.exception.ExceptionHandler;
import com.toyproject.sh.repository.PostRepository;
import com.toyproject.sh.repository.TagManagerRepository;
import com.toyproject.sh.repository.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        int pageLimit = 10; // 한페이지에 보여줄 글 개수

        // 한 페이지당 10개식 글을 보여주고 정렬 기준은 ID기준으로 내림차순
        Page<Post> posts = postRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        Page<PostResponse> postResponse = posts.map(post -> new PostResponse(post));
        return postResponse;
    }

    public Post findSinglePost(Long id) {
        return postRepository.findPostWithComments(id);
    }

    public Page<Post> searchAll(Pageable pageable, String search) {
        int page = pageable.getPageNumber() - 1; // page 위치에 있는 값은 0부터 시작한다.
        int pageLimit = 10; // 한페이지에 보여줄 글 개수

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
}
