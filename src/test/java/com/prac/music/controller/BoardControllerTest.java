package com.prac.music.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prac.music.MockSpringSecurityFilter;
import com.prac.music.config.SecurityConfiguration;
import com.prac.music.domain.board.controller.BoardController;
import com.prac.music.domain.board.dto.BoardRequestDto;
import com.prac.music.domain.board.dto.BoardResponseDto;
import com.prac.music.domain.board.service.BoardService;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = BoardController.class, excludeFilters = {
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = SecurityConfiguration.class
        )
})
public class BoardControllerTest {
    private MockMvc mvc;

    private Principal principal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BoardService boardService;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }

    private Principal getPrincipal() {
        User user = User.builder()
                .userId("asdasdasda")
                .intro("asdaasdasd")
                .email("asdasd@email.com")
                .password("Asdasd!1asd")
                .name("asdasd")
                .build();

        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        return principal = new UsernamePasswordAuthenticationToken(userDetails, "");
    }

    @Test
    @DisplayName("createBoard 성공 테스트")
    public void test1() throws Exception {
        // given
        Principal principal = getPrincipal();

        BoardRequestDto boardRequestDto = BoardRequestDto.builder()
                .title("asdasd")
                .contents("asdasd")
                .build();

        MockMultipartFile file = new MockMultipartFile(
                "files",
                "profileImage.png",
                "multipart/form-data",
                "some image content".getBytes()
        );

        MockMultipartFile mockBoard = new MockMultipartFile(
                "board",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(boardRequestDto)
        );

        BoardResponseDto boardResponseDto = BoardResponseDto.builder()
                .title("asdasd")
                .contents("asdasd")
                .build();

        when(boardService.createBoard(any(BoardRequestDto.class), any(User.class), any(List.class)))
                .thenReturn(boardResponseDto);

        // when - then
        mvc.perform(multipart("/api/boards")
                        .file(mockBoard)
                        .file(file)
                        .principal(principal))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("createBoard title 없을때 테스트")
    public void test2() throws Exception {
        Principal principal = getPrincipal();

        BoardRequestDto boardRequestDto = BoardRequestDto.builder()
                .contents("asdasd")
                .build();

        MockMultipartFile file = new MockMultipartFile(
                "files",
                "profileImage.png",
                "multipart/form-data",
                "some image content".getBytes()
        );

        MockMultipartFile mockBoard = new MockMultipartFile(
                "board",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(boardRequestDto)
        );

        BoardResponseDto boardResponseDto = BoardResponseDto.builder()
                .contents("asdasd")
                .build();

        when(boardService.createBoard(any(BoardRequestDto.class), any(User.class), any(List.class)))
                .thenReturn(boardResponseDto);

        // when - then
        mvc.perform(multipart("/api/boards")
                        .file(mockBoard)
                        .file(file)
                        .principal(principal))
                .andExpect(status().is5xxServerError())
                .andDo(print());
    }

    @Test
    @DisplayName("createBoard contents 없을때 테스트")
    public void test3() throws Exception {
        Principal principal = getPrincipal();

        BoardRequestDto boardRequestDto = BoardRequestDto.builder()
                .title("asdasd")
                .build();

        MockMultipartFile file = new MockMultipartFile(
                "files",
                "profileImage.png",
                "multipart/form-data",
                "some image content".getBytes()
        );

        MockMultipartFile mockBoard = new MockMultipartFile(
                "board",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(boardRequestDto)
        );

        BoardResponseDto boardResponseDto = BoardResponseDto.builder()
                .title("asdasd")
                .build();

        when(boardService.createBoard(any(BoardRequestDto.class), any(User.class), any(List.class)))
                .thenReturn(boardResponseDto);

        // when - then
        mvc.perform(multipart("/api/boards")
                        .file(mockBoard)
                        .file(file)
                        .principal(principal))
                .andExpect(status().is5xxServerError())
                .andDo(print());
    }
    @Test
    @DisplayName("createBoard title,contents 없을때 테스트")
    public void test4() throws Exception {
        Principal principal = getPrincipal();

        BoardRequestDto boardRequestDto = BoardRequestDto.builder()
                .build();

        MockMultipartFile file = new MockMultipartFile(
                "files",
                "profileImage.png",
                "multipart/form-data",
                "some image content".getBytes()
        );

        MockMultipartFile mockBoard = new MockMultipartFile(
                "board",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(boardRequestDto)
        );

        BoardResponseDto boardResponseDto = BoardResponseDto.builder()
                .build();

        when(boardService.createBoard(any(BoardRequestDto.class), any(User.class), any(List.class)))
                .thenReturn(boardResponseDto);

        // when - then
        mvc.perform(multipart("/api/boards")
                        .file(mockBoard)
                        .file(file)
                        .principal(principal))
                .andExpect(status().is5xxServerError())
                .andDo(print());
    }
}
