package com.example.springauth.dto.request;

import com.example.springauth.domain.user.RoleType;
import com.example.springauth.domain.user.UserEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReqUserPostDTO {

    @Valid
    @NotNull(message = "회원 아이디를 입력해주세요.")
    private String username;

    @Valid
    @NotNull(message = "비밀번호를 입력해주세요.")
    private String password;

    @Valid
    @NotNull(message = "닉네임을 입력해주세요.")
    private String nickname;

    public UserEntity toEntity(Long id) {
        return UserEntity.create(id, username, password, nickname);
    }
}
