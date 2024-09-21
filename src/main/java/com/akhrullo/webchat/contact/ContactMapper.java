package com.akhrullo.webchat.contact;

import com.akhrullo.webchat.contact.dto.ContactCreateDto;
import com.akhrullo.webchat.contact.dto.ContactDto;
import com.akhrullo.webchat.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface ContactMapper {

    @Mapping(target = "ownerId", source = "owner.id")
//    @Mapping(target = "userId", source = "user.id")
//    @Mapping(target = "email", source = "user.email")
    ContactDto toDto(Contact contact);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "owner", source = "owner")
//    @Mapping(target = "user", source = "user")
    Contact toEntity(ContactCreateDto createDto, User owner, User user);
}
