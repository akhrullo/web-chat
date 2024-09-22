package com.akhrullo.webchat.chat;

import com.akhrullo.webchat.chat.dto.ChatDto;
import com.akhrullo.webchat.user.dto.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The {@code ChatController} class handles HTTP requests related to chat
 * operations, including creating chats, retrieving chat details, and
 * fetching users in a chat.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/private")
    public ResponseEntity<ChatDto> createChat(@Valid @RequestParam("partner_id") Long partnerId) {
        ChatDto createdChat = chatService.createChat(partnerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChat);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<ChatDto> getChatById(@PathVariable("chatId") Long chatId) {
        ChatDto chatDto = chatService.getChatById(chatId);
        return ResponseEntity.ok(chatDto);
    }

    @GetMapping
    public ResponseEntity<List<ChatDto>> getAllChats() {
        List<ChatDto> chatDtos = chatService.getAllChats();
        return ResponseEntity.ok(chatDtos);
    }

    @GetMapping("/{chatId}/users")
    public ResponseEntity<List<UserDto>> getChatUsers(@PathVariable("chatId") Long chatId) {
        List<UserDto> users = chatService.getChatUsers(chatId);
        return ResponseEntity.ok(users);
    }
}
