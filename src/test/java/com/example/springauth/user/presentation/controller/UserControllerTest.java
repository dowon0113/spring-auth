package com.example.springauth.user.presentation.controller;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
public class UserControllerTest {
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
    @WithMockUser(authorities = "MASTER")  // MASTER 권한 부여
    void 관리자권한부여_성공() throws Exception {
        // given
        Long targetUserId = 777L;
        UserEntity targetUser = UserEntity.builder()
                .id(targetUserId)
                .username("user(AdminGrant)")
                .password("pass")
                .nickname("nick")
                .roleType(RoleType.USER)
                .build();

        userRepository.save(targetUser);

        // when & then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.patch("/v1/users/{userId}/roles", targetUserId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcRestDocumentationWrapper.document("관리자 권한 부여 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("USER v1")
                                .responseFields(
                                        fieldWithPath("username").description("사용자 이름"),
                                        fieldWithPath("nickname").description("닉네임"),
                                        fieldWithPath("roles[].role").description("부여된 역할 (e.g. ADMIN)")
                                )
                                .build()
                        )
                ));
    }

    @Test
    @WithMockUser(authorities = "USER")  // MASTER가 아닌 권한
    void 관리자권한부여_실패_권한없음() throws Exception {
        // given
        Long targetUserId = 778L;
        UserEntity targetUser = UserEntity.builder()
                .id(targetUserId)
                .username("user(NoAuth)")
                .password("pass")
                .nickname("nick")
                .roleType(RoleType.USER)
                .build();

        userRepository.save(targetUser);

        // when & then
        mockMvc.perform(
                        RestDocumentationRequestBuilders.patch("/v1/users/{userId}/roles", targetUserId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error.code").value("ACCESS_DENIED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error.message").value("관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다."))
                .andDo(MockMvcRestDocumentationWrapper.document("관리자 권한 부여 실패 - 권한 없음",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("USER v1")
                                .description("권한 없는 사용자가 관리자 권한을 부여하려고 시도할 경우")
                                .responseFields(
                                        fieldWithPath("error.code").description("에러 코드"),
                                        fieldWithPath("error.message").description("에러 메시지")
                                )
                                .build()
                        )
                ));
    }



}
