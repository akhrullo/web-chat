package com.akhrullo.webchat.config;

import com.akhrullo.webchat.token.Token;
import com.akhrullo.webchat.token.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

/**
 * Service for handling user logout functionality.
 * This service is responsible for invalidating the user's authentication token
 * and clearing any session-related data associated with the logged-in user.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    private final TokenRepository tokenRepository;
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     * Logs out the user by invalidating their authentication token.
     *
     * @param request the HTTP request containing the authorization header
     * @param response the HTTP response to be returned
     * @param authentication the authentication object representing the current user
     */
    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (!isBearerToken(authHeader)) {
            return;
        }

        final String jwt = authHeader.substring(BEARER_PREFIX.length());
        tokenRepository.findByToken(jwt)
                .ifPresentOrElse(this::revokeToken, SecurityContextHolder::clearContext);

        // Clear ThreadLocal session data
        SessionContext.clear();
    }

    private boolean isBearerToken(String authHeader) {
        return authHeader != null && authHeader.startsWith(BEARER_PREFIX);
    }

    private void revokeToken(Token storedToken) {
        storedToken.setExpired(true);
        storedToken.setRevoked(true);
        tokenRepository.save(storedToken);
        SecurityContextHolder.clearContext();
    }
}
