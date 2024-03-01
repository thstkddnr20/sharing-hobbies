package com.toyproject.sh.repository;

import com.toyproject.sh.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c where c.id =:id")
    Comment findOneComment(@Param("id") Long id);
}
