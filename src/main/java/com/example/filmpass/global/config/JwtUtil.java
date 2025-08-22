package com.example.filmpass.global.config;

import com.example.filmpass.domain.user.enums.UserRole;
import com.example.filmpass.global.exception.CustomException;
import com.example.filmpass.global.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class JwtUtil {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final long ACCESS_TOKEN_TIME = 60 * 60 * 1000L; // 만료시간 60분
    private static final long REFRESH_TOKEN_TIME = 60 * 60 * 1000 * 24 * 7L; // 만료시간 7일

    @Value("${SECRET_KEY}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm =  SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        log.info("secretKey(raw) = '{}'", secretKey); // 실제 값 확인용
        byte[] bytes = Base64.getDecoder().decode(secretKey.trim());
        key = Keys.hmacShaKeyFor(bytes);

    }

    // 토큰 생성 메서드
    public String createToken(Long userId, String nickname, UserRole userRole, boolean isCritic) {

        Date date = new Date();

        return BEARER_PREFIX +
             Jwts.builder()
                     .setSubject(String.valueOf(userId))
                     .claim("nickname", nickname)
                     .claim("userRole", userRole.name())
                     .claim("isCritic", isCritic)
                     .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME))
                     .setIssuedAt(date)
                     .signWith(key, signatureAlgorithm)
                     .compact();
    }

    // RefreshToken 생성 메서드
    public String createRefreshToken(Long userId) {
        Date date = new Date();

        return Jwts.builder()
                        .setSubject(String.valueOf(userId))
                        .claim("body", UUID.randomUUID())
                        .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME))
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    // Token 의 만료일을 추출하는 메서드
    public LocalDateTime extractExpiredAt(String token) {

        return extractClaims(token)
                .getExpiration()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    // 토큰에서 bearer 제거
    public String subStringToken(String tokenValue) {

        if(StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        throw new CustomException(ErrorCode.TOKEN_NOT_FOUND);
    }

    // 토큰 추출 메서드
    public Claims extractClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

}
