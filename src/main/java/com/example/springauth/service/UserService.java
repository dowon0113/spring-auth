package com.example.springauth.service;

import com.example.springauth.common.exception.CustomException;
import com.example.springauth.common.exception.GlobalExceptionCode;
import com.example.springauth.domain.user.RoleType;
import com.example.springauth.domain.user.UserEntity;
import com.example.springauth.dto.request.ReqLoginDTO;
import com.example.springauth.dto.request.ReqUserPostDTO;
import com.example.springauth.dto.response.ResLoginDTO;
import com.example.springauth.dto.response.ResUserPostDTO;
import com.example.springauth.dto.response.ResUserRolePatchDTO;
import com.example.springauth.jwt.JwtUtil;
import com.example.springauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

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

    public ResUserRolePatchDTO grantAdminRole(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(GlobalExceptionCode.USER_NOT_FOUND));

        user.updateRole(RoleType.ADMIN);

        return ResUserRolePatchDTO.of(user);
    }
}
