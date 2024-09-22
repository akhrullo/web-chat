package com.akhrullo.webchat.message;

import com.akhrullo.webchat.message.dto.CreateMessageDto;
import com.akhrullo.webchat.message.dto.MessageDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The {@code MessageController} class handles incoming HTTP requests related to messages.
 * It provides endpoints for sending messages, retrieving messages by chat, and marking messages as read.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/messages")
public class MessageController {
    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageDto> sendMessage(@Valid @RequestBody CreateMessageDto messageDto) {
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
}
