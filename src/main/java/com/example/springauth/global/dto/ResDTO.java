package com.example.springauth.global.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ResDTO<T> {
    private ErrorDetail error;

    @Getter
    @AllArgsConstructor
    public static class ErrorDetail {
        private String code;
        private String message;
    }

    public static ResDTO<?> error(String code, String message) {
        return ResDTO.builder()
                .error(new ErrorDetail(code, message))
                .build();
    }
}
