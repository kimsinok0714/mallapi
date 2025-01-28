package com.example.mallapi.controller;

import org.springframework.web.bind.annotation.RestController;
import com.example.mallapi.util.CustomJWTException;
import com.example.mallapi.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;




@RestController
@Slf4j
public class APIRefreshController {

    @PostMapping("/api/member/refresh")
    public Map<String,Object> refresh(@RequestHeader("Authorization")String authHeader, String refreshToken) {

        if (refreshToken == null) {
            throw new CustomJWTException("NULL_REFRESH");
        }

        if (authHeader == null || authHeader.length() < 7) {
            throw new CustomJWTException("INVALID STRING");
        }
        
        String accessToken = authHeader.substring(7);

        // 액세스 토큰이 만료되지 않았다면
        if (!checkExpiredToken(accessToken)) {
            return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        }   

        // 액세스 토큰이 만료되면 리프레시 토큰 검증
        Map<String, Object> claims = JWTUtil.validateToken(refreshToken);
        log.info("Refresh Token : {}", claims);

        String newAccessToken = JWTUtil.generateToken(claims, 10);

        /*            
         * exp : Refresh Token 만료 시간
         * iat : Refresh Token 발행 시간
         */
        String newRefreshToken = checkTime((Integer)claims.get("exp")) == true ? JWTUtil.generateToken(claims, 60*24) : refreshToken;
 
        return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
    }
    
    
    // 액세스 토큰의 만료 여부 확인
    private boolean checkExpiredToken(String token) {
        try {
            JWTUtil.validateToken(token);    
        } catch (Exception e) {
            if(e.getMessage().equals("Expired")) {
                return true;
            }
        }
        return false;       
    }
    

    // Refresh Token이 만료 시간이 1시간 미만인지 여부 확인
    private boolean checkTime(Integer exp) {
        //JWT exp를 날짜로 변환
        java.util.Date expDate = new java.util.Date((long)exp * (1000));

        //현재 시간과의 차이 계산 - ms
        long gap = expDate.getTime() - System.currentTimeMillis();

        // 분단위 계산
        long leftmin = gap / (1000 * 60);

        return leftmin < 60;

    }

}
