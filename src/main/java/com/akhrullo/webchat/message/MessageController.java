package com.akhrullo.webchat.message;

import com.akhrullo.webchat.message.dto.CreateMessageDto;
import com.akhrullo.webchat.message.Message;
import com.akhrullo.webchat.user.User;
import com.akhrullo.webchat.message.MessageService;
import com.akhrullo.webchat.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Class {@code } presents ...
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/messages")
public class MessageController {
    private final UserService userService;
    private final MessageService messageService;

    @PostMapping("/chat/{chatId}")
    public ResponseEntity<Message> sendMessage(@Valid @RequestBody CreateMessageDto createMessageDto,
                                               @RequestHeader("Authorization") String jwt,
                                               @PathVariable Long chatId) {
        User user = userService.findUserByJwt(jwt);
        Message message = messageService.sendMessage(user, chatId, createMessageDto);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<Message>> findChatMessages(@RequestHeader("Authorization") String jwt,
                                               @PathVariable Long chatId) {
        User user = userService.findUserByJwt(jwt);
        List<Message> messages = messageService.findChatMessages(chatId);
        return ResponseEntity.ok(messages);
    }
}
