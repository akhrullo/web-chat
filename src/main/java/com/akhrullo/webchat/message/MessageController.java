package com.akhrullo.webchat.message;

import com.akhrullo.webchat.message.dto.CreateMessageDto;
import com.akhrullo.webchat.message.dto.MessageDto;
import com.akhrullo.webchat.message.dto.MessageFileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * The {@code MessageController} class handles incoming HTTP requests related to messages.
 * It provides endpoints for sending messages, retrieving messages by chat, marking messages as read,
 * and downloading message attachments.
 *
 * <p>APIs:</p>
 * <ul>
 *   <li>Send a message with optional attachment.</li>
 *   <li>Get messages by chat ID with pagination.</li>
 *   <li>Mark messages in a chat as read.</li>
 *   <li>Download an attachment from a message.</li>
 * </ul>
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.1
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/messages")
public class MessageController {
    private final MessageService messageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDto> sendMessage(
            @RequestPart("message") CreateMessageDto messageDto,
            @RequestPart(value = "attachment", required = false) MultipartFile attachment) {
        messageDto.setAttachment(attachment);
        MessageDto sentMessage = messageService.sendMessage(messageDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sentMessage);
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<Page<MessageDto>> getMessagesByChat(
            @PathVariable("chatId") Long chatId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<MessageDto> messages = messageService.getMessagesByChat(chatId, pageable);
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/chat/{chatId}/mark-read")
    public ResponseEntity<Void> markMessagesAsRead(@PathVariable("chatId") Long chatId) {
        messageService.markMessagesAsRead(chatId);
        return ResponseEntity.noContent()
                .build();
    }

    @GetMapping("/{messageId}/download-attachment")
    public ResponseEntity<Resource> downloadAttachment(@PathVariable Long messageId) {
        MessageFileDto fileDto = messageService.getAttachmentForMessage(messageId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileDto.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDto.getFilename() + "\"")
                .body(fileDto.getResource());
    }
}
