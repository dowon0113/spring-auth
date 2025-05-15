package com.example.springauth.user.presentation.controller;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.example.springauth.user.application.dto.request.ReqLoginDTO;
import com.example.springauth.user.application.dto.request.ReqUserPostDTO;
import com.example.springauth.user.domain.entity.UserEntity;
import com.example.springauth.user.domain.repository.UserRepository;
import com.example.springauth.user.domain.vo.RoleType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private String dtoToJson(Object dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }

    @Test
    void 회원가입_성공() throws Exception {
        // given
        ReqUserPostDTO dto = ReqUserPostDTO.builder()
                .username("user(JoinSuccessTest)")
                .password("pass(JoinSuccessTest)")
                .nickname("nick(JoinSuccessTest)")
                .build();

        // when & then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/v1/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(dtoToJson(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(
                        MockMvcRestDocumentationWrapper.document("회원 가입 성공",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(ResourceSnippetParameters.builder()
                                        .tag("AUTH v1")
                                        .build()
                                )
                        )
                );
    }

    @Test
    void 회원가입_실패_중복된_유저네임() throws Exception {
        // given: 먼저 저장된 유저 (중복될 username 사용)
        String duplicateUsername = "user(JoinFailTest)";

        UserEntity existingUser = UserEntity.builder()
                .username(duplicateUsername)
                .password("encodedPassword")
                .nickname("nick(JoinFailTest)")
                .roleType(RoleType.USER)
                .build();

        userRepository.save(existingUser);

        // 같은 username으로 회원가입 시도
        ReqUserPostDTO dto = ReqUserPostDTO.builder()
                .username(duplicateUsername)
                .password("pass(JoinFailTest)")
                .nickname("nick(JoinFailTest2)")
                .build();

        // when & then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/v1/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(dtoToJson(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isConflict()) // 409 충돌 확인
                .andDo(
                        MockMvcRestDocumentationWrapper.document("회원 가입 실패 - 중복 유저네임",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                resource(ResourceSnippetParameters.builder()
                                        .tag("AUTH v1")
                                        .build()
                                )
                        )
                );
    }

    @Test
    void 로그인_성공() throws Exception {
        // given
        String username = "user(LoginSuccessTest)";
        String password = "pass(LoginSuccessTest)";

        // 직접 유저 저장 (비밀번호 인코딩 안 되어 있다면 인코딩 설정 확인 필요)
        UserEntity user = UserEntity.builder()
                .id(100L)
                .username(username)
                .password(password)
                .nickname("nick(LoginSuccessTest)")
                .roleType(RoleType.USER)
                .build();
        userRepository.save(user);

        ReqLoginDTO dto = new ReqLoginDTO(username, password);

        // when & then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(dtoToJson(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcRestDocumentationWrapper.document("로그인 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("AUTH v1")
                                .build()
                        )
                ));
    }

    @Test
    void 로그인_실패_잘못된_비밀번호() throws Exception {
        // given
        String username = "user(LoginFailTest)";
        String password = "correctPassword";

        // 등록된 유저
        UserEntity user = UserEntity.builder()
                .id(100L)
                .username(username)
                .password(password)
                .nickname("nick(LoginFailTest)")
                .roleType(RoleType.USER)
                .build();
        userRepository.save(user);

        ReqLoginDTO dto = new ReqLoginDTO(username, "wrongPassword");

        // when & then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(dtoToJson(dto))
                )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized()) // 401 기대
                .andDo(MockMvcRestDocumentationWrapper.document("로그인 실패 - 잘못된 비밀번호",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("AUTH v1")
                                .responseFields(
                                        fieldWithPath("error.code").description("에러 코드"),
                                        fieldWithPath("error.message").description("에러 메시지")
                                )
                                .build()
                        )
                ));
    }


}
