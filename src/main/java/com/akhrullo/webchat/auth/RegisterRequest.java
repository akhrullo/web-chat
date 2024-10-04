package com.akhrullo.webchat.auth;

import com.akhrullo.webchat.common.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a request for user registration.
 * Contains the necessary information required to create a new user account.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisterRequest {

    /**
     * The first name of the user.
     * Must not be blank.
     */
    @NotBlank
    private String firstname;

    /**
     * The last name of the user.
     * Must not be blank.
     */
    @NotBlank
    private String lastname;

    /**
     * The email address of the user.
     * Must be a valid email format.
     */
    @Email
    private String email;

    /**
     * The password for the user account.
     * Must not be blank.
     */
    @NotBlank
    private String password;

    /**
     * The role assigned to the user during registration.
     * Must not be null.
     */
//    @NotNull
    private UserRole role;
}
