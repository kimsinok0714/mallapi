package com.example.mallapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.mallapi.controller.formatter.LocalDateFormatter;

import lombok.extern.slf4j.Slf4j;


@Configuration
@Slf4j
public class CustomServletConfig implements WebMvcConfigurer { // Spring MVC 설정을 커스터마이징할 때 사용하는 인터페이스

    @Override
    public void addFormatters(FormatterRegistry registry) {   // 데이터를 특정 형식으로 변환하는데 사용

        log.info("{}", "addFormatter");
        
        registry.addFormatter(new LocalDateFormatter());    // 사용자 정의 포매터     
    }

    // // CORS 관련 설정    
    // @Override
    // public void addCorsMappings(CorsRegistry registry) {
    //     // pref-flight
    //     registry.addMapping("/**")
    //         .maxAge(500)
    //         .allowedMethods("GET", "POST", "OPTIONS", "HEAD", "PUT", "DELETE")
    //         .allowedOrigins("*");        
    // }


}
