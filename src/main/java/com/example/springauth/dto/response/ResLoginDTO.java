package com.example.springauth.dto.response;

import com.example.springauth.domain.user.UserEntity;
import com.example.springauth.jwt.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
@AllArgsConstructor
public class ResLoginDTO {
    private String accessToken;

    public static ResLoginDTO of(UserEntity userEntity, JwtUtil jwtUtil) {
        String token = jwtUtil.createToken(
                userEntity.getId(),
                Set.of(userEntity.getRoleType().name())
        );

        return ResLoginDTO.builder()
                .accessToken(token)
                .build();
    }
}
