package com.akhrullo.webchat.auth;


import com.akhrullo.webchat.common.TokenType;
import com.akhrullo.webchat.common.UserRole;
import com.akhrullo.webchat.common.UserState;
import com.akhrullo.webchat.config.CustomUserDetails;
import com.akhrullo.webchat.config.JwtService;
import com.akhrullo.webchat.encryption.keymanagement.KeyManagementService;
import com.akhrullo.webchat.token.Token;
import com.akhrullo.webchat.user.User;
import com.akhrullo.webchat.exception.WebChatApiException;
import com.akhrullo.webchat.token.TokenRepository;
import com.akhrullo.webchat.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Implementation of the {@link AuthenticationService} interface.
 * Provides methods for user registration, authentication, token generation, and refresh.
 * Handles token management and key management for users.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtService jwtService;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final KeyManagementService keyManagementService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        User user = buildUserFromRequest(request);
        User savedUser = repository.save(user);

        String jwtToken = jwtService.generateToken(savedUser);
        String refreshToken = jwtService.generateRefreshToken(savedUser);

        saveUserToken(savedUser, jwtToken);
        keyManagementService.generateAndStoreKeysForUser(savedUser);

        return buildAuthResponse(jwtToken, refreshToken);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticateUser(request);

        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(WebChatApiException::userNotFoundException);

        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return buildAuthResponse(jwtToken, refreshToken);
    }

    private User buildUserFromRequest(RegisterRequest request) {
        return User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.USER)
                .state(UserState.ACTIVE)
                .build();
    }

    private void authenticateUser(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
    }

    private AuthenticationResponse buildAuthResponse(String accessToken, String refreshToken) {
        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokensByUserId(user.getId());
        if (!validUserTokens.isEmpty()) {
            for (Token token : validUserTokens) {
                token.setExpired(true);
                token.setRevoked(true);
            }
            tokenRepository.saveAll(validUserTokens);
        }
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isAuthHeaderInvalid(authHeader)) {
            return;
        }

        String refreshToken = extractTokenFromHeader(authHeader);
        String userEmail = jwtService.extractUsername(refreshToken);

        repository.findByEmail(userEmail).ifPresent(user -> {
            if (jwtService.isTokenValid(refreshToken, CustomUserDetails.from(user))) {
                String accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                writeAuthResponse(response, accessToken, refreshToken);
            }
        });
    }

    private boolean isAuthHeaderInvalid(String authHeader) {
        return authHeader == null || !authHeader.startsWith("Bearer ");
    }

    private String extractTokenFromHeader(String authHeader) {
        return authHeader.substring(7);
    }

    private void writeAuthResponse(HttpServletResponse response, String accessToken, String refreshToken) {
        AuthenticationResponse authResponse = buildAuthResponse(accessToken, refreshToken);
        try {
            new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
        } catch (IOException e) {
            log.error("Error writing authentication response", e);
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing authentication response");
            } catch (IOException ioException) {
                log.error("Error sending error response", ioException);
            }
        }
    }
}

