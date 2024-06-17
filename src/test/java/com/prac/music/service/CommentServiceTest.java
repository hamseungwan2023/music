package com.prac.music.service;

import com.prac.music.domain.board.entity.Board;
import com.prac.music.domain.board.repository.BoardRepository;
import com.prac.music.domain.comment.dto.CommentRequestDto;
import com.prac.music.domain.comment.dto.CommentResponseDto;
import com.prac.music.domain.comment.entity.Comment;
import com.prac.music.domain.comment.repository.CommentRepository;
import com.prac.music.domain.comment.service.CommentService;
import com.prac.music.domain.user.entity.User;
import com.prac.music.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // @Mock 사용을 위해 설정합니다.
public class CommentServiceTest {
    @Mock
    CommentRepository commentRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    BoardRepository boardRepository;

    @InjectMocks
    @Spy
    CommentService commentService;

    User user;
    Board board;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("asdad")
                .userId("fdsafdsfas")
                .password("fdsafsad")
                .build();
        board = Board.builder()
                .id(1L)
                .contents("fdasfd")
                .build();
    }

    @Test
    @DisplayName("댓글 생성 테스트")
    void test1(){
        //given
        CommentRequestDto commentRequestDto =  CommentRequestDto.builder()
                .contents("fdasf")
                .build();

        Comment comment = Comment.builder()
                .id(1L)
                .contents(commentRequestDto.getContents())
                .board(board)
                .user(user)
                .build();
        //when
        commentRepository.save(comment);
        CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
        //then
        assertNotNull(commentResponseDto);
    }
}
