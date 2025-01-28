package com.example.mallapi.service;


import java.util.LinkedHashMap;
import java.util.Optional;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import com.example.mallapi.domain.Member;
import com.example.mallapi.domain.MemberRole;
import com.example.mallapi.dto.MemberDto;
import com.example.mallapi.dto.MemberModifyDto;
import com.example.mallapi.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServieImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    
    @Override
    public MemberDto getKakaoMember(String accessToken) {
        
        String nickname = getNicknameFromKakaoAccessToken(accessToken);

        Optional<Member> result = memberRepository.findById(nickname);
        if (result.isPresent()) {
            MemberDto memberDto = entityToDto(result.get());          
            log.info("existed memberDto : {}", memberDto);
            return memberDto;
        } else {
            Member socialMember = makeSocialMember(nickname);
            memberRepository.save(socialMember);
            log.info("social Member : {}", socialMember);
            return entityToDto(socialMember);
        }                   
    }


    private Member makeSocialMember(String nickname) {
        String tempPassword = makeTempPassword();
        log.info("tempPassword : {}", tempPassword);

        Member member = Member.builder()
            .email(nickname)
            .pw(passwordEncoder.encode(tempPassword))           
            .nickname("Social Member")
            .social(true)
            .build();

        member.addRole(MemberRole.USER);

        return member;
    }


    private String makeTempPassword() {

        StringBuffer buffer = new StringBuffer();
        
        for (int i = 1; i <= 10; i++) {
            buffer.append((char)((int)(Math.random()*55)+65));
        }
        return buffer.toString();

    }


    private String getNicknameFromKakaoAccessToken(String accessToken) {

        // API Server에셔 Kakao API Server에 접근하여 사용자 정보(nickname) 를 조회한다.
        String kakaoGetUserURL = "https://kapi.kakao.com/v2/user/me";
        
        if (accessToken == null) {
            throw new RuntimeException("Access Token is null");
        }

        // API Server 에서 Kakao Rest API 를 호출할 때 사용한다.
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<String>  entity = new HttpEntity<>(headers);
        UriComponents  uriBuilder= UriComponentsBuilder.fromHttpUrl(kakaoGetUserURL).build();
        ResponseEntity<LinkedHashMap> response = restTemplate.exchange(uriBuilder.toString(), HttpMethod.GET, entity, LinkedHashMap.class);

        log.info("response : {}", response); // nickname, id (kakao member id)

        LinkedHashMap<String, LinkedHashMap> bodyMap = response.getBody();
        LinkedHashMap<String, String> kakaoAccount = bodyMap.get("properties");
        log.info("kakaoAccount : {}", kakaoAccount);       
        String nickname = kakaoAccount.get("nickname");        
        log.info("nickname : {}", nickname);

        return nickname;

    }


    @Override
    public void modifyMember(MemberModifyDto memberModifyDto) {
        Optional<Member> result = memberRepository.findById(memberModifyDto.getEmail());

        Member member = result.orElseThrow();
        member.changePw(memberModifyDto.getPw());
        member.changePw(passwordEncoder.encode(memberModifyDto.getPw()));        
        member.changeSocial(false);

        memberRepository.save(member);
        
    }


}
