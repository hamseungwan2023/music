package com.prac.music.service;

import com.prac.music.domain.board.dto.BoardRequestDto;
import com.prac.music.domain.board.dto.BoardResponseDto;
import com.prac.music.domain.board.repository.BoardRepository;
import com.prac.music.domain.board.service.BoardService;
import com.prac.music.domain.user.entity.User;
import com.prac.music.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // @Mock 사용을 위해 설정합니다.
public class BoardServiceTest {
    @Mock
    BoardRepository boardRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    BoardService boardService;

    User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .id(1L)
                .name("asdad")
                .userId("fdsafdsfas")
                .password("fdsafsad")
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user)); // 사용자 조회 설정
    }

    @Test
    @DisplayName("createBoard 성공 시 로직")
    void test1() throws IOException {
        //given
        BoardRequestDto boardRequestDto = BoardRequestDto.builder()
                .title("fdsaf")
                .contents("fdsfdasfas")
                .build();
        List<MultipartFile> files = List.of();
        //when
        BoardResponseDto createdBoard = boardService.createBoard(boardRequestDto,user,files);

        //then
        assertNotNull(createdBoard);
    }
}
