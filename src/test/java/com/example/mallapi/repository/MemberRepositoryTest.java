package com.example.mallapi.repository;


import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.mallapi.domain.Member;
import com.example.mallapi.domain.MemberRole;

import lombok.extern.slf4j.Slf4j;





/*
 * 회원 가입시 패스워드를 반드시 인코딩해서 저장
 */

@SpringBootTest
@Slf4j
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private  PasswordEncoder passwordEncoder;

    // 회원 가입
    @Test
    public void testInsertMember() {
        for (int i = 1; i <= 10 ; i++) {
            Member member = Member.builder()
                .email("user" + i + "@mz.co.kr")          
                .pw(passwordEncoder.encode("1111"))
                .nickname("USER" + i)
                .build();

            member.addRole(MemberRole.USER);
            
            if (i >= 5) {
                member.addRole(MemberRole.MANAGER);
            }

            if (i >= 8) {
                member.addRole(MemberRole.ADMIN);
            }

            memberRepository.save(member);
        }      
        
    }

    // 회원 정보 조회

    @Test
    public void testRead() {
        
        String email = "user9@mz.co.kr";

         Member member = memberRepository.getWithRoles(email).
            orElseThrow(() -> new UsernameNotFoundException("User not found"));            

        log.info("member : {}", member);
        log.info("----------------------------- pw : {}", member.getPw());
        log.info("member.rols : {} ", member.getMemberRoleList());
        System.err.println("pw : " + member.getPw());

    }


}



