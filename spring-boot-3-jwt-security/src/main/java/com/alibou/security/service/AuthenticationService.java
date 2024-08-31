package com.alibou.security.service;

import com.alibou.security.config.JwtService;
import com.alibou.security.domain.User;
import com.alibou.security.domain.UserRole;
import com.alibou.security.dto.AuthenticationRequest;
import com.alibou.security.dto.AuthenticationResponse;
import com.alibou.security.dto.RegisterRequest;
import com.alibou.security.repository.TokenRepository;
import com.alibou.security.repository.UserRepository;
import com.alibou.security.repository.UserRoleRepository;
import com.alibou.security.service.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final UserRoleRepository userRoleRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthenticationResponse register(@NonNull RegisterRequest request) {
        var user = User.builder().firstname(request.getFirstname()).lastname(request.getLastname())
                .email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).build();

        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        if (jwtToken == null || refreshToken == null) {
            throw new NullPointerException("Failed to generate JWT tokens");
        }
        request.getRoles().forEach(role ->
                userRoleRepository.save(UserRole.builder()
                        .roleId(role.getId())
                        .userId(savedUser.getId())
                        .build())
        );
        tokenService.saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
    }

    public AuthenticationResponse authenticate(@NonNull AuthenticationRequest request) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        tokenService.saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
    }

    public void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new NullPointerException();

        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail == null) {
            throw new NullPointerException();
        }
        var user = this.repository.findByEmail(userEmail).orElseThrow();
        var isTokenValid = jwtService.isTokenValid(refreshToken, user.getEmail());
        if (!isTokenValid) {
            throw new NullPointerException();
        }
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        tokenService.saveUserToken(user, accessToken);
        return AuthenticationResponse.builder().accessToken(accessToken).refreshToken(refreshToken)
                .build();
    }


}
