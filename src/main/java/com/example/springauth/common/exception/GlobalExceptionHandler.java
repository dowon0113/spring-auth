package com.example.springauth.common.exception;

import com.example.springauth.common.dto.ResDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
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
        return ResponseEntity.internalServerError().body(body);
    }
}
