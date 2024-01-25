package com.toyproject.sh.repository;

import com.toyproject.sh.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
