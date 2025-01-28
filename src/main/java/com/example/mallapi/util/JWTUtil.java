package com.example.mallapi.util;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.InvalidClaimException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

 /*
  * 1. JWT 문자열 생성(토큰 생성)
  * 2. 토큰 검증
  * 3. 반드시 키 값은 반드시 30자 이상 사용할 것 
  # 4. 반드시 jjwt library 0.11.5 version 사용할 것
  */

@Slf4j
public class JWTUtil {

    // 키 값은 반드시 30자 이상 사용할 것!!
    private static String key = "1234567890123456789012345678901234567890";

    public static String generateToken(Map<String, Object> valueMap, int min) {

        SecretKey key = null;

        try {
            // HMAC-SHA algorithms 
            key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes("UTF-8"));

        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }    

        // JWT는 세 가지 구성 요소(헤더, 페이로드, 서명)로 이루어진 JSON 기반의 토큰

        String jwtStr = Jwts.builder()
            .setHeader(Map.of("typ", "JWT"))  // 헤더 설정 : typ: 토큰 타입
            .setClaims(valueMap)              // JWT의 페이로드(Payload) 설정 : 사용자 정보 , 인증 정보
            .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))   // JWT의 발행 시간
            .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant())) // JWT의 만료 시간
            .signWith(key)  // 서명 : key는 서명에 사용되는 비밀 키, 서명 알고리즘. 서명은 JWT의 변조 여부를 검증하는데 사용
            .compact();
        
        return jwtStr;        
    }


    /*
     * JWT(Json Web Token)를 검증하고, 토큰에 포함된 클레임(Claims) 데이터를 추출하는 로직
     */
    
    public static Map<String, Object> validateToken(String token) {
        
        Map<String, Object> claim = null;

        try {
            // 비밀 키의 역할
            // JWT 생성 시 서명(Signature)을 생성하는 데 사용.
            // 검증 시 서명이 유효한지 확인하는 데 사용.
            SecretKey key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes("UTF-8"));

            // JWT 검증을 수행하기 위한 파서(Parser)를 생성.
            //JWT 서명 검증에 사용할 비밀 키를 설정.
            claim = Jwts.parserBuilder()
                .setSigningKey(key)  
                .build()
                .parseClaimsJws(token)
                .getBody();

        } catch (MalformedJwtException malformedJwtException) {
            throw new CustomJWTException("Malformed");
        } catch (ExpiredJwtException expiredJwtException) {
            throw new CustomJWTException("Expired");
        } catch (InvalidClaimException invalidClaimException) {
            throw new CustomJWTException("Invalid");
        } catch (JwtException jwtException) {
            throw new CustomJWTException("JWTError");
        } catch (Exception ex) {
            throw new CustomJWTException("Error");
        }  
 
        return claim;
    }

}





