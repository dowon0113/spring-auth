package com.example.springauth.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserEntity {

    private String username;
    private String password;
    private String nickname;
    private RoleType roleType;

    @Builder
    public UserEntity(String username, String password, String nickname, RoleType roleType) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.roleType = roleType;
    }

    public static UserEntity create(String username, String password, String nickname) {
        return UserEntity.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .roleType(RoleType.USER)
                .build();
    }
}
