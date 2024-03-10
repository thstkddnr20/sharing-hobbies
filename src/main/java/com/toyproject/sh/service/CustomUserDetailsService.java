package com.toyproject.sh.service;

import com.toyproject.sh.domain.Member;
import com.toyproject.sh.dto.CustomUserDetails;
import com.toyproject.sh.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findMemberByEmail(email);

        if (member != null) {
            return new CustomUserDetails(member);
        }

        return null;
    }
}
