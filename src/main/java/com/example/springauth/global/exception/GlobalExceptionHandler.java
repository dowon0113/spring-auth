package com.example.springauth.global.exception;

import com.example.springauth.global.dto.ResDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResDTO<Void>> handleCustomException(CustomException e) {
        ExceptionCode code = e.getExceptionCode();
        ResDTO<Void> body = ResDTO.<Void>builder()
                .error(new ResDTO.ErrorDetail(code.getCode(), code.getMessage()))
                .build();
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResDTO<Void>> handleUnknown(Exception e) {
        ResDTO<Void> body = ResDTO.<Void>builder()
                .error(new ResDTO.ErrorDetail(GlobalExceptionCode.INTERNAL_ERROR.getCode(), GlobalExceptionCode.INTERNAL_ERROR.getMessage()))
                .build();
        log.error("예상치 못한 서버 예외 발생", e);
        return ResponseEntity.internalServerError().body(body);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResDTO<Void>> handleAccessDeniedException(AccessDeniedException e) {
        ResDTO<Void> body = ResDTO.<Void>builder()
                .error(new ResDTO.ErrorDetail(
                        GlobalExceptionCode.ACCESS_DENIED.getCode(),
                        GlobalExceptionCode.ACCESS_DENIED.getMessage()))
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }
}
