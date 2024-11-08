package com.akhrullo.webchat.contact.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class {@code ContactDto} represents the DTO for a contact.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ContactDto {
    private Long id;

    private String name;

    private Long userId;

    private Long ownerId;

    private String email;
}
