package com.akhrullo.webchat.user.dto;

import com.akhrullo.webchat.common.UserRole;
import com.akhrullo.webchat.common.UserState;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class {@code UserUpdateDto} represents the DTO for updating a user's profile.
 * It contains the fields that can be updated for an existing user.
 * This DTO includes validation constraints to ensure the fields are provided correctly.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserUpdateDto {

    /**
     * The first name of the user.
     * This field cannot be blank.
     */
    @NotBlank(message = "Firstname is required")
    @Size(min = 2, max = 100, message = "Firstname must be between 2 and 100 characters")
    private String firstname;

    /**
     * The last name of the user.
     * This field can be blank.
     */
    private String lastname;

    /**
     * The email address of the user.
     * This field must be a valid email format and cannot be blank.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    /**
     * The state of the user.
     * This field cannot be null.
     */
    @NotNull(message = "State is required")
    private UserState state;

    /**
     * The role of the user.
     * This field cannot be null.
     */
    @NotNull(message = "Role is required")
    private UserRole role;
}

