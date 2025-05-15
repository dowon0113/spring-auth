package com.example.springauth.user.application.service;

import com.example.springauth.global.exception.CustomException;
import com.example.springauth.global.exception.GlobalExceptionCode;
import com.example.springauth.user.domain.vo.RoleType;
import com.example.springauth.user.domain.entity.UserEntity;
import com.example.springauth.user.application.dto.response.ResUserRolePatchDTO;
import com.example.springauth.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public ResUserRolePatchDTO grantAdminRole(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(GlobalExceptionCode.USER_NOT_FOUND));

        user.updateRole(RoleType.ADMIN);

        return ResUserRolePatchDTO.of(user);
    }
}
