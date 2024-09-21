package com.akhrullo.webchat.config;

import com.akhrullo.webchat.token.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthenticationFilter filters incoming requests to validate JWT tokens.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        if (isAuthPath(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = extractJwtToken(request);
        if (jwt != null) {
            authenticateRequest(jwt, request);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isAuthPath(HttpServletRequest request) {
        return request.getServletPath().contains("/api/v1/auth");
    }

    private String extractJwtToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    private void authenticateRequest(String jwt, HttpServletRequest request) {
        String userEmail = jwtService.extractUsername(jwt);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            if (isTokenValid(jwt, userDetails)) {
                setAuthentication(userDetails, request);
            }
        }
    }

    private boolean isTokenValid(String jwt, UserDetails userDetails) {
        boolean isTokenFromRepoValid = tokenRepository.findByToken(jwt)
                .map(token -> !token.isExpired() && !token.isRevoked())
                .orElse(false);

        return jwtService.isTokenValid(jwt, userDetails) && isTokenFromRepoValid;
    }

    private void setAuthentication(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authToken);

        // Assuming userDetails is of type CustomUserDetails
        if (userDetails instanceof CustomUserDetails customUserDetails) {
            // Set user and any additional information into ThreadLocal
            SessionContext.setCurrentUser(customUserDetails.user());
            // You can also set the language or any other session information here
            String lang = request.getHeader("Accept-Language");
            SessionContext.setCurrentLanguage(lang);
        }
    }
}
