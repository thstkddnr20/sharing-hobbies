package com.toyproject.sh.repository;

import com.toyproject.sh.domain.Member;
import com.toyproject.sh.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select p from Post p where p.member =:member")
    Page<Post> findAllByMember(@Param("member") Member member, Pageable pageable);

    @Query("select p from Post p order by p.createdAt desc")
    Page<Post> findAllPost(Pageable pageable);
}
