package com.prac.music;

import com.prac.music.domain.board.entity.Board;
import com.prac.music.domain.board.entity.BoardFiles;
import com.prac.music.domain.board.entity.BoardLike;
import com.prac.music.domain.comment.entity.Comment;
import com.prac.music.domain.comment.entity.CommentLike;
import com.prac.music.domain.mail.entity.Mail;
import com.prac.music.domain.user.entity.User;
import com.prac.music.domain.user.entity.UserStatusEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EntityTest {

    @Test
    @DisplayName("유저 엔티티 테스트")
    public void test1() {
        //given
        User user = userBuild();
        //when

        //then
        assertNotNull(user);
        assertEquals("fdsfadsfdsfas", user.getUserId());
        assertEquals("Asdpassword", user.getPassword());
        assertEquals("fdsafads", user.getName());
        assertEquals("asdasd@email.com", user.getEmail());
        assertEquals("asdadsadasd", user.getIntro());
        assertEquals(UserStatusEnum.TEMPORARY, user.getUserStatusEnum());
        assertEquals("profileImage.png", user.getProfileImage());
    }

    @Test
    @DisplayName("메일 엔티티 테스트")
    public void test2() {
        //given
        User user = userBuild();

        Mail mail = new Mail(user);
        //when

        //then
        assertNotNull(mail);
        assertEquals("asdasd@email.com", mail.getEmail());
        assertEquals("", mail.getCode());
    }

    @Test
    @DisplayName("보드 엔티티 테스트")
    public void test3() {
        //given
        User user = userBuild();
        Board board = boardBuild();
        //when
        //then
        assertNotNull(board);
        assertEquals("title", board.getTitle());
        assertEquals("contents", board.getContents());
    }

    @Test
    @DisplayName("댓글 엔티티 테스트")
    public void test4() {
        //given
        User user = userBuild();
        Board board = boardBuild();
        Comment comment = commentBuild();
        //when

        //then
        assertNotNull(comment);
        assertEquals("contents", comment.getContents());
    }

    @Test
    @DisplayName("보드 좋아요 엔티티 테스트")
    public void test5() {
        //given
        User user = userBuild();
        Board board = boardBuild();
        BoardLike boardLike = new BoardLike(board, user);
        //when

        //then
        assertNotNull(boardLike);
    }

    @Test
    @DisplayName("댓글 좋아요 엔티티 테스트")
    public void test6() {
        //given
        User user = userBuild();
        Board board = boardBuild();
        Comment comment = commentBuild();
        CommentLike commentLike = new CommentLike(comment, user);
        //when

        //then
        assertNotNull(commentLike);
    }

    @Test
    @DisplayName("보드 파일즈 엔티티 테스트")
    public void test7() {
        //given
        User user = userBuild();
        Board board = boardBuild();
        BoardFiles boardFiles = BoardFiles.builder()
                .board(board)
                .file("awsS3Url")
                .build();
        //when

        //then
        assertNotNull(boardFiles);
        assertEquals("awsS3Url", boardFiles.getFile());
    }

    public User userBuild() {
        return User.builder()
                .userId("fdsfadsfdsfas")
                .password("Asdpassword")
                .name("fdsafads")
                .email("asdasd@email.com")
                .intro("asdadsadasd")
                .userStatusEnum(UserStatusEnum.TEMPORARY)
                .profileImage("profileImage.png")
                .build();
    }

    public Board boardBuild() {
        return Board.builder()
                .title("title")
                .contents("contents")
                .user(userBuild())
                .build();
    }

    public Comment commentBuild() {
        return Comment.builder()
                .board(boardBuild())
                .user(userBuild())
                .contents("contents")
                .build();
    }
}
