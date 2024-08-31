package com.alibou.security.module;

import com.alibou.security.config.JwtService;
import com.alibou.security.domain.Role;
import com.alibou.security.domain.Token;
import com.alibou.security.domain.User;
import com.alibou.security.domain.UserRole;
import com.alibou.security.dto.*;
import com.alibou.security.repository.TokenRepository;
import com.alibou.security.repository.UserRepository;
import com.alibou.security.repository.UserRoleRepository;
import com.alibou.security.service.AuthenticationService;
import com.alibou.security.service.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class AuthenticationServiceUniTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository repository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private TokenService tokenService;

    //    @InjectMocks
    private AuthenticationService authenticationService;

    private User userTest;
    private Token token1;
    private Token token2;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        authenticationService = spy(new AuthenticationService(repository,userRoleRepository, tokenRepository, passwordEncoder, jwtService, authenticationManager,tokenService));
        userTest = new User();
        userTest.setId(UUID.randomUUID());
        token1 = new Token();
        token1.setExpired(false);
        token1.setRevoked(false);

        token2 = new Token();
        token2.setExpired(false);
        token2.setRevoked(false);
    }

    @Test
    void testRegister() {
        // Arrange
        Role role = Role.builder().name(RoleType.USER).build();
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        RegisterRequest request = RegisterRequest.builder().firstname("John").lastname("Doe")
                .email("john.doe@example.com").password("password123").roles(roles).build();

        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password("encodedPassword")
                .build();

        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(repository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("jwtToken");
        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");

        // Act
        AuthenticationResponse response = authenticationService.register(request);

        // Assert
        assertNotNull(response);
        assertEquals("jwtToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());

        verify(passwordEncoder).encode(request.getPassword());
        verify(repository).save(any(User.class));
        verify(jwtService).generateToken(user);
        verify(jwtService).generateRefreshToken(user);
        // Verify saveUserToken was called correctly
        verify(tokenService, times(1)).saveUserToken(user, "jwtToken");

    }

    @Test
    void testRegisterEmptyPassword() {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.builder().id(UUID.randomUUID()).name(RoleType.USER).build());
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setFirstname("John");
        request.setLastname("Doe");
        request.setEmail("john.doe@example.com");
        request.setPassword(""); // Mật khẩu rỗng
        request.setRoles(roles);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            authenticationService.register(request);
        }, "Password cannot be empty");

        // Kiểm tra các phương thức không bị gọi khi mật khẩu rỗng
        verify(repository, times(1)).save(any(User.class));
        verify(userRoleRepository, times(0)).save(any(UserRole.class));
        verify(tokenService, times(0)).saveUserToken(any(User.class), any(String.class));
    }

    @Test
    void testRegisterUserWithInvalidEmail() {
        Role role = Role.builder().name(RoleType.USER).build();
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setFirstname("John");
        request.setLastname("Doe");
        request.setEmail("invalid-email"); // Email không hợp lệ
        request.setPassword("password123");
        request.setRoles(roles);

        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(repository.save(any(User.class))).thenThrow(new IllegalArgumentException("Invalid email"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authenticationService.register(request));

        verify(tokenRepository, never()).save(any(Token.class));
    }

    @Test
    void testRegisterJwtTokenGeneration_jwtTokenFailure() {
        Role role = Role.builder().name(RoleType.USER).build();
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setFirstname("John");
        request.setLastname("Doe");
        request.setEmail("john.doe@example.com");
        request.setPassword("password123");
        request.setRoles(roles);

        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password("encodedPassword")
                .build();

        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(repository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn(null); // JWT token không được tạo

        // Act & Assert
        assertThrows(NullPointerException.class, () -> authenticationService.register(request));

        verify(tokenRepository, never()).save(any(Token.class));
    }

    @Test
    void testRegisterJwtTokenGeneration_jwtRefreshTokenFailure() {
        Role role = Role.builder().name(RoleType.USER).build();
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setFirstname("John");
        request.setLastname("Doe");
        request.setEmail("john.doe@example.com");
        request.setPassword("password123");
        request.setRoles(roles);

        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password("encodedPassword")
                .build();

        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(repository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn(null); // JWT refreshToken không được tạo

        // Act & Assert
        assertThrows(NullPointerException.class, () -> authenticationService.register(request));

        verify(tokenRepository, never()).save(any(Token.class));
    }

    @Test
    void testRegister_NullRequest_ShouldThrowNullPointerException() {

        // Act & Assert
        assertThrows(NullPointerException.class, () -> authenticationService.register(null));
    }

    @Test
    void testAuthenticate_Success() {
        AuthenticationRequest authRequest = AuthenticationRequest.builder()
                .email("example@example.com")
                .password("securePassword123")
                .build();
        User user = User.builder()
                .email("abc@gmail/com")
                .password("encodedPassword")
                .build();
        // Arrange
        when(authenticationManager.authenticate(any())).thenReturn(new UsernamePasswordAuthenticationToken("abc@gmail.com", "password"));
        when(repository.findByEmail(any())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refreshToken");
//        doNothing().when(authenticationService).revokeAllUserTokens(user);

        // Act
        AuthenticationResponse response = authenticationService.authenticate(authRequest);

        // Assert
        assertEquals("jwtToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());


        verify(repository).findByEmail(any());
        verify(jwtService).generateToken(any(User.class));
        verify(jwtService).generateRefreshToken(any(User.class));
//        verify(authenticationService).revokeAllUserTokens(any(User.class));
        verify(tokenService, times(1)).saveUserToken(user, "jwtToken");
    }

    @Test
    void testAuthenticate_UserNotFound_ShouldThrowException() {
        AuthenticationRequest authRequest = AuthenticationRequest.builder()
                .email("example@example.com")
                .password("securePassword123")
                .build();
        // Arrange
        when(authenticationManager.authenticate(any())).thenReturn(new UsernamePasswordAuthenticationToken("abc@gmail.com", "password"));
        when(repository.findByEmail(any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> authenticationService.authenticate(authRequest));
    }

    @Test
    void testAuthenticate_NullRequest_ShouldThrowNullPointerException() {

        // Act & Assert
        assertThrows(NullPointerException.class, () -> authenticationService.authenticate(null));
    }

    @Test
    void testRevokeAllUserTokens_WithValidTokens() {
        // Arrange
        List<Token> validTokens = List.of(token1, token2);

        when(tokenRepository.findAllValidTokenByUser(userTest.getId())).thenReturn(validTokens);

        // Act
        authenticationService.revokeAllUserTokens(userTest);

        // Assert
        assertTrue(token1.isExpired());
        assertTrue(token1.isRevoked());
        assertTrue(token2.isExpired());
        assertTrue(token2.isRevoked());

        verify(tokenRepository).findAllValidTokenByUser(userTest.getId());
        verify(tokenRepository).saveAll(validTokens);
    }

    @Test
    void testRevokeAllUserTokens_WithNoValidTokens() {
        // Arrange
        when(tokenRepository.findAllValidTokenByUser(userTest.getId())).thenReturn(List.of());

        // Act
        authenticationService.revokeAllUserTokens(userTest);

        // Assert
        verify(tokenRepository).findAllValidTokenByUser(userTest.getId());
        verify(tokenRepository, never()).saveAll(anyList());
    }

    @Test
    void refreshToken_NoAuthHeader() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

        assertThrows(NullPointerException.class, () -> authenticationService.refreshToken(request));

        verifyNoInteractions(jwtService, repository, tokenRepository);
    }

    @Test
    void refreshToken_InvalidHeader() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("InvalidHeader");

        assertThrows(NullPointerException.class, () -> authenticationService.refreshToken(request));

        verifyNoInteractions(jwtService, repository, tokenRepository);
    }

    @Test
    void refreshToken_UserNotFound() {
        String refreshToken = "refreshToken";
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + refreshToken);
        when(jwtService.extractUsername(refreshToken)).thenReturn(null);

        assertThrows(NullPointerException.class, () -> authenticationService.refreshToken(request));

        verify(jwtService).extractUsername(refreshToken);
    }


    @Test
    void refreshToken_ErrorIsTokenValid() {
        String refreshToken = "refreshToken";
        String userEmail = "user@example.com";
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + refreshToken);
        when(jwtService.extractUsername(refreshToken)).thenReturn(userEmail);
        when(repository.findByEmail(userEmail)).thenReturn(Optional.of(User.builder().build()));
        when(jwtService.isTokenValid(anyString(), anyString())).thenReturn(false);

        assertThrows(NullPointerException.class, () -> authenticationService.refreshToken(request));
    }

    @Test
    void refreshToken_Success() {
        String refreshToken = "refreshToken";
        String userEmail = "user@example.com";
        String newAccessToken = "newAccessToken";
        User user = new User(); // Create and set up a User object as needed
        user.setEmail(userEmail);
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer " + refreshToken);
        when(jwtService.extractUsername(refreshToken)).thenReturn(userEmail);
        when(repository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid(anyString(), anyString())).thenReturn(true);
        when(jwtService.generateToken(any())).thenReturn("newAccessToken");
        doNothing().when(authenticationService).revokeAllUserTokens(any(User.class));
        doNothing().when(tokenService).saveUserToken(any(User.class),anyString());

        // Act
        var response = authenticationService.refreshToken(request);

        assertNotNull(response);
        assertEquals(newAccessToken, response.getAccessToken());
        verify(authenticationService).revokeAllUserTokens(any(User.class));
        verify(tokenService).saveUserToken(any(User.class),anyString());
    }
}
