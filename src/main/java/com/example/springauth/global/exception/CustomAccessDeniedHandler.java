package com.example.springauth.global.exception;

import com.example.springauth.global.dto.ResDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        log.warn("접근 거부: {}", accessDeniedException.getMessage());

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");

        ResDTO<Void> body = ResDTO.<Void>builder()
                .error(new ResDTO.ErrorDetail("ACCESS_DENIED", "관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다."))
                .build();

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
    }
}

