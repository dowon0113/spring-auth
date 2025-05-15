package com.example.springauth.user.infrastructure.jwt;

import com.example.springauth.global.exception.CustomException;
import com.example.springauth.global.exception.GlobalExceptionCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.debug("authHeader = {}", authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Authorization 헤더가 없거나 잘못됨");
            filterChain.doFilter(request, response); // 다음 필터로 넘김 (익명 접근 허용된 경우)
            return;
        }

        try {
            String token = jwtUtil.stripBearerPrefix(authHeader);
            log.debug("token = {}", token);
            jwtUtil.validateToken(token); // 서명 및 만료 검증

            Long userId = jwtUtil.getUserId(token);
            Set<String> roles = jwtUtil.getRoles(token);

            Set<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());

            Authentication auth = new UsernamePasswordAuthenticationToken(userId, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (ExpiredJwtException e) {
            throw new CustomException(GlobalExceptionCode.INVALID_TOKEN);
        } catch (JwtException | IllegalArgumentException e) {
            throw new CustomException(GlobalExceptionCode.INVALID_TOKEN);
        } catch (Exception e) {
            log.error("알 수 없는 예외 발생: {}", e.getMessage(), e);
            throw new CustomException(GlobalExceptionCode.INTERNAL_ERROR);
        }

        filterChain.doFilter(request, response);
    }
}
