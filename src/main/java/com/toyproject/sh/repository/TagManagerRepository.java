package com.toyproject.sh.repository;

import com.toyproject.sh.domain.Member;
import com.toyproject.sh.domain.Post;
import com.toyproject.sh.domain.TagManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TagManagerRepository extends JpaRepository<TagManager, Long> {
    @Query("select tm from TagManager tm where tm.tag.name =:name and tm.member =:member")
    Optional<TagManager> findTMByNameAndMember(@Param("name") String name, @Param("member") Member member);

    @Query("select tm.tag.name from TagManager tm where tm.member =:member")
    String findTagWithMember(@Param("member") Member member);

    @Query("select tm.tag.name from TagManager tm where tm.post =:post")
    String findTagWithPost(@Param("post") Post post);

    @Query("select tm from TagManager tm where tm.post =:post")
    Optional<TagManager> findTMByPost(@Param("post") Post post);

    // 태그이름으로 포스트를 찾고 생성날짜 기준으로 내림차순
    @Query("select tm.post from TagManager tm where tm.tag.name =:tagName order by tm.post.createdAt desc")
    Page<Post> findPostByTagName(@Param("tagName") String tagName, Pageable pageable);

}
