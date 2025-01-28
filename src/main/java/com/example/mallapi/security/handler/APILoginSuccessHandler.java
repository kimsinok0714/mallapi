package com.example.mallapi.security.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.example.mallapi.dto.MemberDto;
import com.example.mallapi.util.JWTUtil;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


/*
 * jwt.io 사이트에서 토큰 검증
 */
@Slf4j
public class APILoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {
        
            
        log.info("authentication : {} ", authentication);        
        
        // UsernamePasswordAuthenticationToken 
        // [Principal=com.example.mallapi.dto.MemberDto [Username=user1@mz.co.kr, Password=[PROTECTED], 
        // Enabled=true, AccountNonExpired=true, credentialsNonExpired=true, AccountNonLocked=true, Granted Authorities=[ROLE_USER]], 
        // Credentials=[PROTECTED], Authenticated=true, Details=WebAuthenticationDetails [RemoteIpAddress=0:0:0:0:0:0:0:1, SessionId=null], Granted Authorities=[ROLE_USER]] 
                
        MemberDto memberDto = (MemberDto)authentication.getPrincipal();

        Map<String, Object> claims = memberDto.getClaims();

        // JWT 인증 토큰 생성
        String accessToken = JWTUtil.generateToken(claims, 10);    // 10분
        String refreshToken = JWTUtil.generateToken(claims, 60 * 24);  // 1일

 
        // 추가 정보 : Access Token, Refresh Token(교환권)
        // 토큰 기반의 데이터 전송의 문제는 Hooking이므로 유효 시간을 짧게 
        claims.put("accessToken", accessToken);
        claims.put("refreshToken", refreshToken);
        
        
        // claims -> JSON 문자열
        Gson gson = new Gson();        
        String jsonStr = gson.toJson(claims);

        response.setContentType("application/json; charset=utf-8");
        PrintWriter pw = response.getWriter();
        pw.println(jsonStr);
        pw.close();

    }

}
