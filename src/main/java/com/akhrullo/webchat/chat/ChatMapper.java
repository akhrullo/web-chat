package com.akhrullo.webchat.chat;

import com.akhrullo.webchat.chat.dto.ChatDto;
import com.akhrullo.webchat.user.User;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The {@code ChatMapper} interface maps between {@link Chat} entities and
 * {@link ChatDto} data transfer objects using MapStruct.
 *
 * <p>Provides methods to create private and group chats and
 * extract user IDs from user entities.</p>
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

    @Mapping(target = "userIds", source = "users", qualifiedByName = "userIdsFromUsers")
    ChatDto toDto(Chat chat);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "messages", ignore = true)
    @Mapping(target = "type", constant = "PRIVATE")
    @Mapping(target = "users", expression = "java(java.util.List.of(user, partner))")
    Chat toPrivateChat(User user, User partner);

    @Named("userIdsFromUsers")
    default List<Long> mapUserIds(List<User> users) {
        return users.stream()
                .map(User::getId)
                .collect(Collectors.toList());
    }
}
