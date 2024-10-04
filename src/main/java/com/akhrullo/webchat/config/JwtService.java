package com.akhrullo.webchat.config;

import com.akhrullo.webchat.config.properties.JwtProperties;
import com.akhrullo.webchat.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
/**
 * Service class for handling JWT-related operations.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtProperties jwtProperties;

    /**
     * Extracts the username (subject) from the JWT token.
     *
     * @param token the JWT token
     * @return the username, or null if extraction fails
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject)
                .orElse(null);
    }

    /**
     * Extracts a specific claim from the JWT token.
     *
     * @param token the JWT token
     * @param claimsResolver the function to resolve the claim from claims
     * @param <T> the type of claim to extract
     * @return an Optional containing the claim, or empty if extraction fails
     */
    public <T> Optional<T> extractClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = extractAllClaims(token);
            return Optional.ofNullable(claimsResolver.apply(claims));
        } catch (JwtException e) {
            log.warn("Failed to extract claim from JWT token: {}", e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Generates a JWT token for the given user.
     *
     * @param user the user
     * @return the generated JWT token
     */
    public String generateToken(User user) {
        return generateToken(new HashMap<>(), user);
    }

    /**
     * Generates a JWT token with additional claims.
     *
     * @param extraClaims additional claims to include in the token
     * @param user the user
     * @return the generated JWT token
     */
    public String generateToken(Map<String, Object> extraClaims, User user) {
        return buildToken(extraClaims, getUserDetails(user), jwtProperties.getExpiration());
    }

    /**
     * Generates a refresh token for the user.
     *
     * @param user the user
     * @return the generated refresh token
     */
    public String generateRefreshToken(User user) {
        return buildToken(new HashMap<>(), getUserDetails(user), jwtProperties.getRefreshTokenExpiration());
    }

    /**
     * Builds a JWT token with the specified claims and expiration.
     *
     * @param extraClaims the extra claims
     * @param userDetails the user details
     * @param expiration the expiration time in milliseconds
     * @return the generated JWT token
     */
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Checks if a JWT token is valid for the specified user details.
     *
     * @param token the JWT token
     * @param userDetails the user details
     * @return true if the token is valid, false otherwise
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Checks if the JWT token is expired.
     *
     * @param token the JWT token
     * @return true if the token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token)
                .map(expiration -> expiration.before(new Date()))
                .orElse(true); // Consider token expired if expiration cannot be determined
    }

    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token the JWT token
     * @return an Optional containing the expiration date, or empty if extraction fails
     */
    private Optional<Date> extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts all claims from the JWT token.
     *
     * @param token the JWT token
     * @return the claims
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Gets the signing key used to verify JWT tokens.
     *
     * @return the signing key
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Gets the user details for the given user.
     *
     * @param user the user
     * @return the custom user details
     */
    public CustomUserDetails getUserDetails(User user) {
        return CustomUserDetails.from(user);
    }
}
