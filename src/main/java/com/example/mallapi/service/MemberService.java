package com.example.mallapi.service;

import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import com.example.mallapi.domain.Member;
import com.example.mallapi.dto.MemberDto;
import com.example.mallapi.dto.MemberModifyDto;

@Transactional
public interface MemberService {

    MemberDto getKakaoMember(String accessToken);

    void modifyMember(MemberModifyDto memberModifyDto);
    

    default MemberDto entityToDto(Member member) {

        MemberDto dto = new MemberDto(
            member.getEmail(),
            member.getPw(), 
            member.getNickname(), 
            member.isSocial(), 
            member.getMemberRoleList().stream().map(memberRole -> memberRole.name()).collect(Collectors.toList()));

        return dto;
    }

}
