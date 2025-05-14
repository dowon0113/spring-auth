package com.example.springauth.service;

import com.example.springauth.common.exception.CustomException;
import com.example.springauth.common.exception.GlobalExceptionCode;
import com.example.springauth.domain.user.UserEntity;
import com.example.springauth.dto.request.ReqUserPostDTO;
import com.example.springauth.dto.response.ResUserPostDTO;
import com.example.springauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public ResUserPostDTO save(ReqUserPostDTO dto){
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new CustomException(GlobalExceptionCode.USER_ALREADY_EXISTS);
        }

        UserEntity user = dto.toEntity();

        userRepository.save(user);

        return ResUserPostDTO.of(user);
    }
}
