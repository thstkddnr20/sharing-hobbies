package com.toyproject.sh.repository;

import com.toyproject.sh.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    Member findMemberByEmailAndPassword(String email, String password);
}
