package com.example.springauth.jwt;

import com.example.springauth.common.exception.CustomException;
import com.example.springauth.common.exception.GlobalExceptionCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtUtil {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String ROLE_CLAIM_KEY = "roles";

    @Value("${jwt.expiration}")
    private long expirationTime;

    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    @PostConstruct
    public void init() {
        byte[] secretBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(secretBytes);
    }

    /**
     * JWT 토큰 생성
     */
    public String createToken(Long userId, Set<String> roles) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setSubject(userId.toString())
                .claim(ROLE_CLAIM_KEY, List.copyOf(roles)) //Set → List 변환
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 토큰 유효성 검증
     */
    public void validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token); // 서명 및 만료 검증
        } catch (ExpiredJwtException e) {
            log.warn("JWT 만료됨");
            throw new CustomException(GlobalExceptionCode.INVALID_TOKEN);
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("유효하지 않은 JWT");
            throw new CustomException(GlobalExceptionCode.INVALID_TOKEN);
        }
    }

    /**
     * 사용자 ID 추출
     */
    public Long getUserId(String token) {
        return Long.parseLong(getClaims(token).getSubject());
    }

    /**
     * 역할 정보 추출
     */
    @SuppressWarnings("unchecked")
    public Set<String> getRoles(String token) {
        Object rawRoles = getClaims(token).get("roles");

        log.debug("roles 원시 타입: {}", rawRoles.getClass());
        log.debug("roles 값: {}", rawRoles);

        if (rawRoles instanceof List<?> list) {
            return list.stream().map(Object::toString).collect(Collectors.toSet());
        }

        throw new CustomException(GlobalExceptionCode.INVALID_TOKEN);
    }


    /**
     * Claims 추출
     */
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Bearer prefix 제거
     */
    public String stripBearerPrefix(String token) {
        if (token != null && token.startsWith(BEARER_PREFIX)) {
            return token.substring(BEARER_PREFIX.length());
        }
        return token;
    }
}
