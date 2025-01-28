package com.example.mallapi.security.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.mallapi.dto.MemberDto;
import com.example.mallapi.util.JWTUtil;
import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


// OncePerRequestFilter : HTTP 요청 마다 실행되도록 보장되는 필터를 구현
@Component
@Slf4j
public class JWTCheckFilter extends OncePerRequestFilter {
    
    /*
     * false : check, true : not check
     */

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {        

        // Preflight 요청은 Access Token을 체크하지 않는다.
        // Preflight 요청 (CORS Preflight Request)
        // 웹 브라우저가 CORS 정책을 따르는 서버에 HTTP 요청을 보내기 전에, 요청이 허용될지를 확인하기 위해 사전에 보내는 OPTIONS 요청입니다.      

        String path = request.getRequestURI();     
        log.info("path : {}", path);      
        
        // api/member/ 경로의 호출은  Access Token을 체크하지 않는다.
        if (request.getMethod().equals("OPTIONS") || path.startsWith("/api/member") || path.startsWith("/api/products/view/")) {
            return true;
        }
                
        return false;                
        
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {  
     
        String authHeader = request.getHeader("Authorization");
        log.info("Authorization Header : {}", authHeader);
           
        try {
            // [Bearer][공백][JWT문자열]
            String accessToken = authHeader.substring(7);
            Map<String,Object> claims = JWTUtil.validateToken(accessToken);
            log.info("claims : {}", claims);

            String email = (String)claims.get("email");
            String pw = (String)claims.get("pw");
            String nickname = (String)claims.get("nickname");
            Boolean social = (Boolean)claims.get("social");
            List<String> roleNames = (List<String>)claims.get("roleNames");           

            MemberDto memberDto = new MemberDto(email, nickname, pw, social.booleanValue(), roleNames);

            log.info("memberDto : {}", memberDto);

            /*
             * UsernamePasswordAuthenticationToken :                      
             * 인증이 성공하면, 사용자 정보(principal), credentials, 권한 목록(authorities), 그리고 추가 정보를 포함하는 인증 객체로 사용된다.
             * 이 객체는 Spring Security의 SecurityContext에 저장된다.
             */            
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberDto, pw, memberDto.getAuthorities()); 

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            
            filterChain.doFilter(request, response);                

        } catch(Exception e) {
            log.info("Error : {}", e.getMessage());

            Gson gson = new Gson();
            String msg = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));

            response.setContentType("application/json");
            PrintWriter pw = response.getWriter();
            pw.println(msg);
            pw.close();
        }
        
    }

}

