package com.example.springauth.dto.response;

import com.example.springauth.domain.user.RoleType;
import com.example.springauth.domain.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResUserPostDTO {
    private String username;
    private String nickname;
    private List<Role> roles;

    public static ResUserPostDTO of(UserEntity userEntity) {
        return ResUserPostDTO.builder()
                .username(userEntity.getUsername())
                .nickname(userEntity.getNickname())
                .roles(List.of(new Role(userEntity.getRoleType())))
                .build();
    }

    @Getter
    @AllArgsConstructor
    public static class Role {
        private RoleType role;
    }
}
