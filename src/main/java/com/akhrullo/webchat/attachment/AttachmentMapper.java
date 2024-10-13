package com.akhrullo.webchat.attachment;

import org.mapstruct.Mapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.Named;

/**
 * Mapper interface for converting between Attachment entities and AttachmentDto.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface AttachmentMapper {

    @Mapping(target = "media", source = "type", qualifiedByName = "isMedia")
    AttachmentDto toDto(Attachment attachment);

    @Named("isMedia")
    default boolean isMedia(String type) {
        return type != null &&
                (
                        type.startsWith("image/jpeg") ||
                        type.startsWith("image/png") ||
                        type.equals("video/mp4")
                );
    }
}
