package com.example.mallapi.config;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.mallapi.security.filter.JWTCheckFilter;
import com.example.mallapi.security.handler.APILoginSuccessHandler;
import com.example.mallapi.security.handler.ApiLoginFailHandler;
import com.example.mallapi.security.handler.CustomAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/*
 * Spring 3.1 Version 부터 많이 변경됨
 */
@Configuration
@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class CustomSecurityConfig {

    /*
     * 1. CSRF 비활성화
     * 2. CORS 설정
     * 3. 세션 비활성화
     */    

   
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    
        
        log.info("------------------------ security config");

        
        http.cors(httpSecurityCorsConfigurer -> {
            httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
        });

        http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        
        http.csrf(config -> config.disable());

       
        http.formLogin(config -> {
            //username, password
            config.loginPage("/api/member/login");
            config.successHandler(new APILoginSuccessHandler());      
            config.failureHandler(new ApiLoginFailHandler());
        });


        // UsernamePasswordAuthenticationFilter 보다 JWTCheckFilter 를 먼저 수행한다.
        http.addFilterBefore(new JWTCheckFilter(), UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling(config -> {
             config.accessDeniedHandler(new CustomAccessDeniedHandler());
        });        

        return http.build();    
    } 

    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
