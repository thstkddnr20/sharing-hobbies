package com.toyproject.sh.repository;

import com.toyproject.sh.domain.Member;
import com.toyproject.sh.domain.Post;
import com.toyproject.sh.dto.PostAndTagNameDto;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select p from Post p where p.member =:member")
    Page<Post> findAllByMember(@Param("member") Member member, Pageable pageable);

    @Query("select p from Post p order by p.createdAt desc")
    Page<Post> findAllPost(Pageable pageable);

    @Query("select p from Post p left join fetch p.comments where p.count =:id")
        // 단일 게시글과 해당 게시글의 댓글 모두 조회
    Post findPostWithComments(@Param("id") Long id);

    @Query("SELECT new com.toyproject.sh.dto.PostAndTagNameDto(p, t.name) FROM Post p left join TagManager tm ON p = tm.post left join Tag t ON tm.tag = t WHERE p.count = :id")
    PostAndTagNameDto findPostAndTagName(@Param("id") Long id);

    @Query("select p from Post p where p.thumbnail like :thumbnail")
        // 제목을 기준으로 포스트 검색
    Page<Post> findPostByThumbnail(@Param("thumbnail") String thumbnail, Pageable pageable);

    @Query("select max(p.count) from Post p")
    Long findMaxCount();

    @Query("select p.member.email from Post p where p.count = :id")
    String findPostAuthor(@Param("id") Long id);

    @Query("select p from Post p where p.count =:id")
    Post findOnePost(@Param("id") Long id);

    @Modifying
    @Query("delete from Post p where p.count =:id")
    void deletePostById(@Param("id") Long id);
}
