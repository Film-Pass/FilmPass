package com.example.filmpass.global.config;

import com.example.filmpass.domain.user.enums.UserRole;
import com.example.filmpass.global.exception.CustomException;
import com.example.filmpass.global.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 토큰이 비었는지 검증
        String bearerJwt = request.getHeader("Authorization");

        if(bearerJwt == null || !bearerJwt.startsWith("Bearer")) {
            log.error("JWT 필터에서 예외 발생 - 토큰이 없음.");
            filterChain.doFilter(request, response);
        }

        // bear 제거
        String token = jwtUtil.subStringToken(bearerJwt);


        // payload 파싱오류시 예외발생
        try {
            Claims claims = jwtUtil.extractClaims(token);

            // 사용자 정보 추출
            Long userId = Long.valueOf(claims.getSubject());
            String nickname = String.valueOf(claims.get("nickname"));
            UserRole userRole = UserRole.of(claims.get("userRole", String.class));

            // Security Context 에 저장
            UserPrincipal userPrincipal = new UserPrincipal(userId, nickname, userRole);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userPrincipal,
                    null,
                    userPrincipal.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        } catch (JwtException | IllegalArgumentException e) {
            log.error("JWT 필터에서 예외 발생 - 잘못된 토큰값 입력");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write("{\"message\": \"잘못된 JWT 토큰입니다.\"}");
            return;
        }

        filterChain.doFilter(request, response);




    }
}
