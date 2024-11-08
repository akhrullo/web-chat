package com.akhrullo.webchat.config;

import com.akhrullo.webchat.token.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

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
    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;

    @Value("${auth.with-cookies}")
    private boolean authWithCookies;

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

        final String jwt = authWithCookies ? extractTokenFromCookies(request) : extractJwtToken(request);
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

    private String extractTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> "accessToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
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
        boolean isStoredTokenActive = tokenService.isTokenValid(jwt);
        return jwtService.isTokenValid(jwt, userDetails) && isStoredTokenActive;
    }

    private void setAuthentication(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authToken);

        if (userDetails instanceof CustomUserDetails customUserDetails) {
            // Set user and any additional information into ThreadLocal
            SessionContext.setCurrentUser(customUserDetails.user());
            String lang = request.getHeader("Accept-Language");
            SessionContext.setCurrentLanguage(lang);
        }
    }
}
