package com.example.springauth.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlobalExceptionCode implements ExceptionCode {

    USER_ALREADY_EXISTS("USER_ALREADY_EXISTS", "이미 가입된 사용자입니다."),
    USER_NOT_FOUND("USER_NOT_FOUND", "사용자를 찾을 수 없습니다."),
    UNAUTHORIZED("UNAUTHORIZED", "인증이 필요합니다."),
    INTERNAL_ERROR("INTERNAL_ERROR", "서버 오류가 발생했습니다."),
    INVALID_TOKEN("INVALID_TOKEN","유효하지 않은 인증 토큰입니다."),
    INVALID_CREDENTIALS("INVALID_CREDENTIALS", "비밀번호가 일치하지 않습니다."),
    ACCESS_DENIED("ACCESS_DENIED", "접근 권한이 없습니다."),;

    private final String code;
    private final String message;
}
