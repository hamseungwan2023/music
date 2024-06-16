package com.prac.music.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prac.music.MockSpringSecurityFilter;
import com.prac.music.config.SecurityConfiguration;
import com.prac.music.domain.user.controller.UserController;
import com.prac.music.domain.user.dto.LoginRequestDto;
import com.prac.music.domain.user.dto.SignoutRequestDto;
import com.prac.music.domain.user.dto.SignupRequestDto;
import com.prac.music.domain.user.entity.User;
import com.prac.music.domain.user.security.UserDetailsImpl;
import com.prac.music.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = UserController.class, excludeFilters = {
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = SecurityConfiguration.class
        )
})
public class UserControllerTest {
    private MockMvc mvc;

    private Principal principal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }

    @Test
    @DisplayName("회원가입 요청")
    public void test1() throws Exception {
        //given
        SignupRequestDto requestDto = SignupRequestDto.builder()
                .userId("asdasdasda")
                .intro("asdaasdasd")
                .email("asdasd@email.com")
                .password("Asdasd!1asd")
                .name("asdasd")
                .build();

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "profileImage.png",
                "multipart/form-data",
                "some image content".getBytes()
        );

        MockMultipartFile user = new MockMultipartFile(
                "user",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(requestDto)
        );

        // when - then
        mvc.perform(multipart("/api/users/signup")
                        .file(user)
                        .file(file))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 요청")
    public void test2() throws Exception {
        //given
        LoginRequestDto requestDto = LoginRequestDto.builder()
                .userId("asdasd")
                .password("fdsfasdf")
                .build();

        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        //when - then
        mvc.perform(post("/api/users/login")
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("로그아웃 요청")
    public void test3() throws Exception {
        //given
        User user = User.builder()
                .userId("fdsafadsfd")
                .build();
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        principal = new UsernamePasswordAuthenticationToken(userDetails, "");
        //when
        userService.logoutUser(userDetails.getUser());
        //then
        mvc.perform(put("/api/users/logout")
                        .principal(principal))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("회원 탈퇴 요청")
    public void test4() throws Exception {
        //given
        User user = User.builder()
                .id(1L)
                .password("$2a$10$e8ZlP.GdmX5zGmz9qlNtIu4nF3qK26IhAdIpdEuxQeA9vB9lF1oL.") // 암호화된 비밀번호
                .build();

        SignoutRequestDto requestDto = SignoutRequestDto.builder()
                .password("fdsafadsf")
                .build();

        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        Authentication principal = new UsernamePasswordAuthenticationToken(userDetails, "");
        //when

        //then
        mvc.perform(put("/api/users/signout")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(print());
    }
}