package com.akhrullo.webchat.message.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating a new message in the chat application.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateMessageDto {
    @NotBlank(message = "Message content cannot be blank")
    private String content;

    private String image;

    @NotNull(message = "Receiver ID cannot be null")
    private Long receiverId;

    @NotNull(message = "Chat ID cannot be null")
    private Long chatId;
}
