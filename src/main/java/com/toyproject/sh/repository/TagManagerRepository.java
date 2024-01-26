package com.toyproject.sh.repository;

import com.toyproject.sh.domain.TagManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TagManagerRepository extends JpaRepository<TagManager, Long> {
    @Query("select tm from TagManager tm where tm.tag.name =:name")
    Optional<TagManager> findTagManagerByTagName(@Param("name") String name);
}
