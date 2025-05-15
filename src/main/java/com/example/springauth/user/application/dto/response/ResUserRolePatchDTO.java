package com.example.springauth.user.application.dto.response;

import com.example.springauth.user.domain.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ResUserRolePatchDTO {

    private String username;
    private String nickname;
    private List<RoleDTO> roles;

    public static ResUserRolePatchDTO of(UserEntity user) {
        return ResUserRolePatchDTO.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .roles(List.of(new RoleDTO(user.getRoleType().name())))
                .build();
    }

    @Getter
    @AllArgsConstructor
    public static class RoleDTO {
        private String role;
    }
}
