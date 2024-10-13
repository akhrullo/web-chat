package com.akhrullo.webchat.message.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.core.io.Resource;

/**
 * Data Transfer Object for representing a file associated with a message.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@Data
@Builder
public class MessageFileDto {

    private String contentType;

    private Resource resource;

    private String filename;
}
