package com.akhrullo.webchat.session;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

/**
 * Service for managing HTTP cookies for access and refresh tokens.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@Service
public class CookieManagementService {
    private static final String ACCESS_TOKEN_COOKIE_NAME = "accessToken";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    private static final int ACCESS_TOKEN_EXPIRY = 60 * 60; // 1 hour
    private static final int REFRESH_TOKEN_EXPIRY = 60 * 60 * 24 * 30; // 30 days

    /**
     * Sets the access token cookie.
     *
     * @param response the HTTP response
     * @param token the access token
     */
    public void setAccessTokenCookie(HttpServletResponse response, String token) {
        Cookie accessTokenCookie = createCookie(ACCESS_TOKEN_COOKIE_NAME, token, ACCESS_TOKEN_EXPIRY);
        response.addCookie(accessTokenCookie);
    }

    /**
     * Sets the refresh token cookie.
     *
     * @param response the HTTP response
     * @param token the refresh token
     */
    public void setRefreshTokenCookie(HttpServletResponse response, String token) {
        Cookie refreshTokenCookie = createCookie(REFRESH_TOKEN_COOKIE_NAME, token, REFRESH_TOKEN_EXPIRY);
        response.addCookie(refreshTokenCookie);
    }

    /**
     * Creates a cookie with specified attributes.
     *
     * @param name the cookie name
     * @param value the cookie value
     * @param expiry the cookie expiry in seconds
     * @return the configured {@link Cookie}
     */
    private Cookie createCookie(String name, String value, int expiry) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(expiry);
        return cookie;
    }
}
