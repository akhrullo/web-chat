package com.akhrullo.webchat.contact.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Class {@code ContactCreateDto} represents the DTO for creating a contact.
 * It contains the necessary fields that must be provided when adding a new contact.
 * This DTO includes validation constraints to ensure that the required fields are provided.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ContactCreateDto {

    /**
     * The name of the contact.
     * This field cannot be null or blank.
     */
    @NotBlank(message = "Contact name must not be blank")
    @Size(min = 2, max = 100, message = "Contact name must be between 2 and 100 characters")
    private String name;

    /**
     * The ID of the user associated with the contact.
     * This field cannot be null.
     */
    @NotNull(message = "User ID is required")
    private Long userId;
}

