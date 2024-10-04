package com.akhrullo.webchat.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for JWT security settings.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "application.security.jwt")
public class JwtProperties {
    /**
     * The secret key used for signing JWT tokens.
     */
    private String secretKey;

    /**
     * The expiration time for the access token in milliseconds.
     */
    private Long expiration;

    /**
     * Properties related to refresh tokens.
     */
    private Long refreshTokenExpiration;
}
