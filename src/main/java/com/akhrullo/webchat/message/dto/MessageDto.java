package com.akhrullo.webchat.message.dto;

import com.akhrullo.webchat.attachment.AttachmentDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for representing a message.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MessageDto {

    private Long id;

    private String content;

    private Long senderId;

    private Long chatId;

    private boolean isRead;

    private boolean isSent;

    private AttachmentDto attachment;
}
