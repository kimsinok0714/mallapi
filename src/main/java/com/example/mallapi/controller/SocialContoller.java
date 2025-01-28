package com.example.mallapi.controller;

import org.springframework.web.bind.annotation.RestController;
import com.example.mallapi.dto.MemberDto;
import com.example.mallapi.dto.MemberModifyDto;
import com.example.mallapi.service.MemberService;
import com.example.mallapi.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@Slf4j
@RequiredArgsConstructor
public class SocialContoller {

    private final MemberService memberService;

    @GetMapping("/api/member/kakao")
    public Map<String, Object> getMemberFromKakao(@RequestParam("accessToken") String accessToken) {

        log.info("accessToken : {}", accessToken);

        MemberDto memberDto = memberService.getKakaoMember(accessToken);
        
        Map<String, Object> claims = memberDto.getClaims();

        String jwtAccessToken = JWTUtil.generateToken(claims, 10);
        String jwtRefreshToken = JWTUtil.generateToken(claims, 60*24);

        claims.put("accessToken", jwtAccessToken);
        claims.put("refreshToken", jwtRefreshToken);

        return claims;        
    }    


    @PutMapping("/api/member/modify")
    public Map<String,String> modify(@RequestBody MemberModifyDto memberModifyDto) {
        log.info("memberModifyDto : {}", memberModifyDto);
        memberService.modifyMember(memberModifyDto);
        return Map.of("result", "modified");        
    }

}
