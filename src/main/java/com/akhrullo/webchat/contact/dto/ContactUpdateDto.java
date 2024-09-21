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
 * Class {@code ContactUpdateDto} represents the DTO for updating a contact.
 * It contains the fields that can be modified when updating an existing contact.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ContactUpdateDto {

    /**
     * The updated name of the contact.
     */
    @NotBlank(message = "Contact name must not be blank")
    @Size(min = 2, max = 100, message = "Contact name must be between 2 and 100 characters")
    private String name;

    /**
     * The ID of the user associated with the contact.
     */
    @NotNull(message = "User ID must not be null")
    private Long userId;
}

