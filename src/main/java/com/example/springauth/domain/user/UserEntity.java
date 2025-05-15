package com.example.springauth.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserEntity {

    private Long id;
    private String username;
    private String password;
    private String nickname;
    private RoleType roleType;

    @Builder
    public UserEntity(Long id,String username, String password, String nickname, RoleType roleType) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.roleType = roleType;
    }

    public static UserEntity create(Long id,String username, String password, String nickname) {
        return UserEntity.builder()
                .id(id)
                .username(username)
                .password(password)
                .nickname(nickname)
                .roleType(RoleType.USER)
                .build();
    }

    public void updateRole(RoleType newRole) {
        this.roleType = newRole;
    }
}
