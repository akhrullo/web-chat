package com.akhrullo.webchat.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Service interface for handling user authentication and registration operations.
 * Defines methods for user registration, authentication, and refreshing JWT tokens.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
public interface AuthenticationService {

    /**
     * Registers a new user and generates JWT tokens (access and refresh).
     *
     * @param request the registration request containing user details
     * @return an {@link AuthenticationResponse} containing the access and refresh tokens
     */
    AuthenticationResponse register(RegisterRequest request);

    /**
     * Authenticates a user based on provided credentials and generates new JWT tokens.
     *
     * @param request the authentication request containing the user's email and password
     * @return an {@link AuthenticationResponse} containing the access and refresh tokens
     */
    AuthenticationResponse authenticate(AuthenticationRequest request);

    /**
     * Refreshes the user's JWT access token using a valid refresh token.
     *
     * @param request  the HTTP request containing the refresh token in the authorization header
     * @param response the HTTP response where the new tokens will be written
     */
    void refreshToken(HttpServletRequest request, HttpServletResponse response);
}
