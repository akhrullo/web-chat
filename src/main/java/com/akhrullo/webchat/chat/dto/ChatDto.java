package com.akhrullo.webchat.chat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;


/**
 * The {@code ChatDto} class is a data transfer object for chat information,
 * including chat ID, name, image, and associated partner.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChatDto {
    private Long id;
    private String name;
    private String image;
    private ChatPartnerDto partner;
}
