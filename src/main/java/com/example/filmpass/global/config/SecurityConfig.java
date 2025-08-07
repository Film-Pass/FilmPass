package com.example.filmpass.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // JwtFitler 를 Bean 으로 등록
    @Bean
    public JwtFilter jwtFilter(JwtUtil jwtUtil) {
        return new JwtFilter(jwtUtil);
    }

    // security 필터 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JwtUtil jwtUtil) throws Exception {

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)  // csrf 비활성화
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable) // 폼 로그인 비활성화
                .addFilterBefore(jwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class) // jwtFilter 추가
                .authorizeHttpRequests(auth -> auth
// 인증 필요없는 요청들
                        .requestMatchers("/api/auth/signup", "/api/auth/login","/").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/movies/").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/movies/search").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/seat/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/movies/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/theaters/*", "/api/movies/*"
                        ,"/api/theaters", "/api/movies").permitAll()
                        .anyRequest().authenticated()
                )
                .build();

    }

    // Password Encoder 를 Bean 으로 등록
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
