package com.akhrullo.webchat.chat;

import com.akhrullo.webchat.chat.dto.ChatDto;
import com.akhrullo.webchat.chat.dto.ChatPartnerDto;
import com.akhrullo.webchat.contact.dto.ContactDto;
import com.akhrullo.webchat.user.User;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;


/**
 * The {@code ChatMapper} interface maps between {@link Chat} entities and
 * {@link ChatDto} data transfer objects using MapStruct.
 *
 * @see Chat
 * @see ChatDto
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ChatMapper {

    @Mapping(target = "id", source = "chat.id")
    @Mapping(target = "partner", source = "partner")
    @Mapping(target = "name", source = "partner.name")
    ChatDto toDto(Chat chat, ChatPartnerDto partner);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "messages", ignore = true)
    @Mapping(target = "type", constant = "PRIVATE")
    @Mapping(target = "users", expression = "java(java.util.List.of(user, partner))")
    Chat toPrivateChat(User user, User partner);

    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "contactId", source = "id")
    @Mapping(target = "contact", constant = "true")
    ChatPartnerDto toPartnerDto(ContactDto contactDto);

    @Mapping(target = "userId", source = "id")
    @Mapping(target = "contactId", ignore = true)
    @Mapping(target = "contact", constant = "false")
    @Mapping(target = "name", source = "user", qualifiedByName = "mapToName")
    ChatPartnerDto toPartnerDto(User user);

    @Named("mapToName")
    default String mapToName(User user) {
        return user.getFirstname() + " " + user.getLastname();
    }
}
