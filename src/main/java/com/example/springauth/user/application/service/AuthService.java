package com.example.springauth.user.application.service;

import com.example.springauth.global.exception.CustomException;
import com.example.springauth.global.exception.GlobalExceptionCode;
import com.example.springauth.user.application.dto.request.ReqLoginDTO;
import com.example.springauth.user.application.dto.request.ReqUserPostDTO;
import com.example.springauth.user.application.dto.response.ResLoginDTO;
import com.example.springauth.user.application.dto.response.ResUserPostDTO;
import com.example.springauth.user.domain.entity.UserEntity;
import com.example.springauth.user.domain.repository.UserRepository;
import com.example.springauth.user.infrastructure.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public ResUserPostDTO save(ReqUserPostDTO dto){
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new CustomException(GlobalExceptionCode.USER_ALREADY_EXISTS);
        }

        // ID 수동 생성
        Long newId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        System.out.println("회원id:"+newId);

        UserEntity user = dto.toEntity(newId);

        userRepository.save(user);

        return ResUserPostDTO.of(user);
    }

    public ResLoginDTO login(ReqLoginDTO dto) {
        UserEntity user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new CustomException(GlobalExceptionCode.USER_NOT_FOUND));

        if (!user.getPassword().equals(dto.getPassword())) {
            throw new CustomException(GlobalExceptionCode.INVALID_CREDENTIALS);
        }

        return ResLoginDTO.of(user, jwtUtil);
    }
}
