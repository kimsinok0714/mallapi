package com.example.mallapi.security.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


// API 서버는 인증에 실패한 경우 반드시 에러 메시지를 전송한다. : 상태 코드 : 200

@Slf4j
public class ApiLoginFailHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException exception) throws IOException, ServletException {
                
        log.info("exception : {}", exception);

        Gson gson = new Gson();
        
        String jsonStr = gson.toJson(Map.of("error" ,"ERROR_LOGIN"));

        response.setContentType("application/json");
        PrintWriter pw = response.getWriter();
        pw.println(jsonStr);
        pw.close();

        
    }

    
}
