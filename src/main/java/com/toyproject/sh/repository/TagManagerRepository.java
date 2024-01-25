package com.toyproject.sh.repository;

import com.toyproject.sh.domain.TagManager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagManagerRepository extends JpaRepository<TagManager, Long> {
}
