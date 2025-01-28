package com.example.mallapi.security;


import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.mallapi.domain.Member;
import com.example.mallapi.dto.MemberDto;
import com.example.mallapi.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@Primary
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    
 
    // username => id에 해당하는 값
    // User extends UserDetails`
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("------------------------- loadUserByUsername : " + username);

        Member member = memberRepository.getWithRoles(username)
            .orElseThrow(() -> new UsernameNotFoundException(username + "not found") );

        log.info("member : {}", member);
                
        // Principal
        MemberDto memberDto = new MemberDto(
            member.getEmail(),
            member.getPw(),
            member.getNickname(),                        
            member.isSocial(),
            member.getMemberRoleList().stream().map(memberRole -> memberRole.name()).collect(Collectors.toList()));
           
        log.info("memberDto : {}", memberDto);
        // [Username=user1@mz.co.kr, Password=[PROTECTED], Enabled=true, AccountNonExpired=true, CredentialsNonExpired=true, AccountNonLocked=true, Granted Authorities=[ROLE_USER]]
      
        return memberDto;
    }

    
}
