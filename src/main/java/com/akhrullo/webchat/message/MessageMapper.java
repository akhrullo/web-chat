package com.akhrullo.webchat.message;

import com.akhrullo.webchat.chat.Chat;
import com.akhrullo.webchat.message.dto.CreateMessageDto;
import com.akhrullo.webchat.message.dto.MessageDto;
import com.akhrullo.webchat.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * The {@code MessageMapper} interface provides mapping methods to convert between
 * {@code Message}, {@code CreateMessageDto}, and {@code MessageDto} objects.
 * It uses MapStruct for the mapping implementations.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface MessageMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    @Mapping(target = "attachmentId", ignore = true)
    @Mapping(target = "chat", source = "chat")
    @Mapping(target = "read", constant = "false")
    @Mapping(target = "sender", source = "sender")
    Message toEntity(CreateMessageDto messageDto, Chat chat, User sender);

    @Mapping(target = "isSent", ignore = true)
    @Mapping(target = "attachment", ignore = true)
    @Mapping(target = "isRead", source = "read")
    @Mapping(target = "chatId", source = "chat.id")
    @Mapping(target = "senderId", source = "sender.id")
    MessageDto toDto(Message savedMessage);
}
