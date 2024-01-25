package com.toyproject.sh.repository;

import com.toyproject.sh.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
