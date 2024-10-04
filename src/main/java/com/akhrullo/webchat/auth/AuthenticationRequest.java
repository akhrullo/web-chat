package com.akhrullo.webchat.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO representing the authentication request with user credentials.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticationRequest {
    /**
     * The user's email address.
     * Must be a valid email format.
     */
    @Email
    private String email;

    /**
     * The user's password.
     * Cannot be blank.
     */
    @NotBlank
    private String password;
}
