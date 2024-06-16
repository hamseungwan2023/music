package com.prac.music.service;

import com.prac.music.common.exception.UserServiceException;
import com.prac.music.domain.user.dto.LoginRequestDto;
import com.prac.music.domain.user.dto.LoginResponseDto;
import com.prac.music.domain.user.entity.User;
import com.prac.music.domain.user.repository.UserRepository;
import com.prac.music.domain.user.service.JwtService;
import com.prac.music.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class) // @Mock 사용을 위해 설정합니다.
public class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    JwtService jwtService;

    @InjectMocks
    @Spy
    UserService userService;


    @Test
    @DisplayName("로그인 성공 시 로직")
    void test1() {
        // given
        User user = User.builder()
                .userId("asdasd")
                .password("asdasd")
                .build();
        userRepository.save(user);

        LoginRequestDto requestDto = mock(LoginRequestDto.class);
        when(requestDto.getUserId()).thenReturn("asdasd");
        when(requestDto.getPassword()).thenReturn("asdasd");
        when(userRepository.findByUserId(requestDto.getUserId())).thenReturn(Optional.of(user));

        // when
        LoginResponseDto loggedInUser = userService.loginUser(requestDto);

        // then
        assertNotNull(loggedInUser);
    }

    @Test
    @DisplayName("로그인시 아이디 넣지 않을때 로직")
    void test2() {
        //given
        User user = User.builder()
                .userId("asdasd")
                .password("asdasd")
                .build();
        userRepository.save(user);
        LoginRequestDto requestDto = Mockito.mock(LoginRequestDto.class);
        when(requestDto.getUserId()).thenReturn("null");
        when(requestDto.getPassword()).thenReturn("asdasd");
        userRepository.findByUserId(requestDto.getUserId());
        //when
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getUserId(),
                        requestDto.getPassword()));
        //then
        assertThrows(UserServiceException.class, () -> userService.loginUser(requestDto));
    }

    @Test
    @DisplayName("로그인시 비밀번호를 넣지 않았을때 로직")
    void test3() {
        //given
        User user = User.builder()
                .userId("asdasd")
                .password("asdasd")
                .build();

        LoginRequestDto requestDto = Mockito.mock(LoginRequestDto.class);
        userRepository.findByUserId(requestDto.getUserId());
        when(requestDto.getUserId()).thenReturn("asdasd");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getUserId(),
                        requestDto.getPassword()));

        //then
        assertThrows(UserServiceException.class, () -> userService.loginUser(requestDto));
    }
}
