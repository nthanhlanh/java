package com.alibou.security.module;

import com.alibou.security.domain.User;
import com.alibou.security.dto.ChangePasswordRequest;
import com.alibou.security.dto.UserDto;
import com.alibou.security.mapper.UserMapper;
import com.alibou.security.repository.UserRepository;
import com.alibou.security.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceUniTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testChangePassword_Success() {

        // Arrange
        User mockUser = User.builder()
                .password("encodedCurrentPassword")
                .build();

        Principal principal = new UsernamePasswordAuthenticationToken(mockUser, null);

        ChangePasswordRequest request = ChangePasswordRequest.builder().currentPassword("currentPassword")
                .newPassword("newPassword").confirmationPassword("newPassword").build();

        when(passwordEncoder.matches(request.getCurrentPassword(), mockUser.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(request.getNewPassword())).thenReturn("encodedNewPassword");

        // Act
        userService.changePassword(request, principal);

        // Assert
        verify(repository).save(mockUser); // Verify that repository.save() was called
        // Optionally, you can check if the user's password was correctly set
        assertEquals("encodedNewPassword", mockUser.getPassword()); // Directly check the password field
    }

    @Test
    void testChangePassword_WrongCurrentPassword() {
        // Arrange
        User mockUser = User.builder()
                .password("encodedCurrentPassword")
                .build();

        Principal principal = new UsernamePasswordAuthenticationToken(mockUser, null);

        ChangePasswordRequest request = ChangePasswordRequest.builder().currentPassword("wrongPassword")
                .newPassword("newPassword").confirmationPassword("newPassword").build();

        when(passwordEncoder.matches(request.getCurrentPassword(), mockUser.getPassword())).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> userService.changePassword(request, principal));
        verify(repository, never()).save(any());
    }

    @Test
    void testChangePassword_NewPasswordsDoNotMatch() {
        // Arrange
        User mockUser = User.builder()
                .password("encodedCurrentPassword")
                .build();

        Principal principal = new UsernamePasswordAuthenticationToken(mockUser, null);

        ChangePasswordRequest request = ChangePasswordRequest.builder().currentPassword("wrongPassword")
                .newPassword("newPassword").confirmationPassword("differentPassword").build();

        when(passwordEncoder.matches(request.getCurrentPassword(), mockUser.getPassword())).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> userService.changePassword(request, principal));
        verify(repository, never()).save(any());
    }

    @Test
    void getUserById() {
        User mockUser = User.builder().id(UUID.randomUUID()).build();

        // Mock hành vi của myRepository
        when(repository.findById(any(UUID.class))).thenReturn(Optional.ofNullable(mockUser));

        // Mock hành vi của userMapper
        UserDto mockUserDto = UserDto.builder().id(UUID.randomUUID()).email("abc@gmail.com").build(); // Giả sử bạn có class UserDto
        when(userMapper.toDto(mockUser)).thenReturn(mockUserDto);

        // Gọi phương thức cần test
        var result = userService.getUserById(UUID.randomUUID());

        // kim tra kết quả
        assertNotNull(result);
        assertNotNull(result.getId());
    }

    @Test
    void testGetUserById_NullPointerException() {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(repository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            userService.getUserById(userId);
        });
    }
}
