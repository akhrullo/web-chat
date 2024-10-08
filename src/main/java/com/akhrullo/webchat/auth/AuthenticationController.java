package com.akhrullo.webchat.auth;

import com.akhrullo.webchat.session.CookieManagementService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling authentication-related requests.
 * Provides endpoints for user registration, authentication, and token refreshing.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;
    private final CookieManagementService cookieManagementService;

    /**
     * Registers a new user with the provided registration details.
     *
     * @param request the registration request containing user information.
     * @return a response entity containing the authentication response (e.g., JWT token).
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }

    /**
     * Authenticates an existing user with the provided credentials.
     *
     * @param request  the authentication request containing username and password.
     * @param response the HTTP response to write the cookies.
     * @return a response entity containing the authentication response (e.g., JWT token).
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request,
            HttpServletResponse response
    ) {
        AuthenticationResponse authenticationResponse = service.authenticate(request);

        cookieManagementService.setAccessTokenCookie(response, authenticationResponse.getAccessToken());
        cookieManagementService.setRefreshTokenCookie(response, authenticationResponse.getRefreshToken());

        return ResponseEntity.ok(authenticationResponse);
    }

    /**
     * Refreshes the JWT token for the authenticated user.
     *
     * @param request the HTTP request containing the current token.
     * @param response the HTTP response to write the refreshed token.
     */
    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        service.refreshToken(request, response);
    }
}
