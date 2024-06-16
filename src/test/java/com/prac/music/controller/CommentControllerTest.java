package com.prac.music.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prac.music.MockSpringSecurityFilter;
import com.prac.music.config.SecurityConfiguration;
import com.prac.music.domain.comment.controller.CommentController;
import com.prac.music.domain.comment.dto.CommentRequestDto;
import com.prac.music.domain.comment.dto.CommentResponseDto;
import com.prac.music.domain.comment.service.CommentService;
import com.prac.music.domain.user.entity.User;
import com.prac.music.domain.user.security.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = CommentController.class, excludeFilters = {
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = SecurityConfiguration.class
        )
})
public class CommentControllerTest {
    private MockMvc mvc;

    private Principal principal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }


    private void getPrincipal() {
        User user = User.builder()
                .id(1L)
                .userId("asdasdasda")
                .intro("asdaasdasd")
                .email("asdasd@email.com")
                .password("Asdasd!1asd")
                .name("asdasd")
                .build();

        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        principal = new UsernamePasswordAuthenticationToken(userDetails, "");
    }


    @Test
    @DisplayName("createComment 성공 테스트")
    void test1() throws Exception {
        // given
        this.getPrincipal();

        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .contents("fdasfadsf")
                .build();

        CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                .contents(commentRequestDto.getContents())
                .boardId(1L)
                .userId(1L)
                .build();

        when(commentService.createComment(any(CommentRequestDto.class), any(Long.class), any(User.class)))
                .thenReturn(commentResponseDto);

        String jsonRequest = objectMapper.writeValueAsString(commentRequestDto);
        // when / then
        mvc.perform(post("/api/boards/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .principal(principal))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("createComment 유저 없을 시 테스트")
    void test2() throws Exception {
        // given
        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .contents("fdasfadsf")
                .build();

        CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                .contents(commentRequestDto.getContents())
                .boardId(1L)
                .userId(1L)
                .build();

        when(commentService.createComment(any(CommentRequestDto.class), any(Long.class), any(User.class)))
                .thenReturn(commentResponseDto);

        String jsonRequest = objectMapper.writeValueAsString(commentRequestDto);
        // when / then
        mvc.perform(post("/api/boards/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().is5xxServerError())
                .andDo(print());
    }

    @Test
    @DisplayName("createUser 입력값 없이 요청 테스트")
    void test3() throws Exception {
        // given
        this.getPrincipal();

        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .build();

        CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                .contents(commentRequestDto.getContents())
                .boardId(1L)
                .userId(1L)
                .build();

        when(commentService.createComment(any(CommentRequestDto.class), any(Long.class), any(User.class)))
                .thenReturn(commentResponseDto);

        String jsonRequest = objectMapper.writeValueAsString(commentRequestDto);
        // when / then
        mvc.perform(post("/api/boards/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .principal(principal))
                .andExpect(status().is5xxServerError())
                .andDo(print());
    }
}
