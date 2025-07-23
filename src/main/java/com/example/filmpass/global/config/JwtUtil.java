package com.example.filmpass.global.config;

import com.example.filmpass.domain.user.enums.UserRole;
import com.example.filmpass.global.exception.CustomException;
import com.example.filmpass.global.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    private static final String BEARER_PREFIX ="Bearer ";
    private static final long TOKEN_TIME = 60 * 60 * 1000L; // 만료시간 60분

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm =  SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {

        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);

    }

    // 토큰 생성 메서드
    public String createToken(Long userId, String nickname, UserRole userRole) {

        Date date = new Date();

        return BEARER_PREFIX +
             Jwts.builder()
                     .setSubject(String.valueOf(userId))
                     .claim("nickname", nickname)
                     .claim("userRole", userRole.name())
                     .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                     .setIssuedAt(date)
                     .signWith(key, signatureAlgorithm)
                     .compact();
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
