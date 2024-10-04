package com.akhrullo.webchat.controller;

import com.akhrullo.webchat.chat.ChatService;
import com.akhrullo.webchat.chat.dto.ChatDto;
import com.akhrullo.webchat.config.SessionContext;
import com.akhrullo.webchat.contact.ContactService;
import com.akhrullo.webchat.message.MessageService;
import com.akhrullo.webchat.message.dto.MessageDto;
import com.akhrullo.webchat.user.UserService;
import com.akhrullo.webchat.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Class {@code } presents ...
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatModelController {


    private final ChatService chatService;
    private final UserService userService;
    private final ContactService contactService;
    private final MessageService messageService;

    @GetMapping("/{chatId}")
    public String viewChat(@PathVariable("chatId") Long chatId, Model model) {
        // Fetch chat details
//        User userById = userService.findUserById(2L);
//        SessionContext.setCurrentUser(userById);
        ChatDto chat = chatService.getChatById(chatId);
        List<UserDto> chatUsers = chatService.getChatUsers(chatId);

        // Fetch messages for the chat
        List<MessageDto> messages = messageService.getMessagesByChat(chatId, PageRequest.of(0, 50)).getContent();

        // Fetch partner details (assumes private chat with 2 users)
        UserDto partner = chatUsers.stream()
                .filter(user -> !user.getId().equals(SessionContext.getCurrentUser().getId())) // Find the other user
                .findFirst()
                .orElse(null);

        // Add attributes to the model
        model.addAttribute("contacts", userService.findUserById(1L).getContacts()); // Assuming current user is auto-detected
        model.addAttribute("chatId", chat.getId());
        model.addAttribute("messages", messages);
        model.addAttribute("partnerName", partner != null ? partner.getFirstname() + " " + partner.getLastname() : "Unknown");
        model.addAttribute("partnerEmail", partner != null ? partner.getEmail() : "Unknown");
        model.addAttribute("partnerPhone", partner != null ? "+990 90 900 00 99" : "Unknown");
        model.addAttribute("partnerId", partner != null ? partner.getId() : 0);

        return "chat";
    }

    @GetMapping
    public String viewAllChats(Model model) {
        // Retrieve all chats for the user
        List<ChatDto> chats = chatService.getAllChats();

        // Add chats to model
        model.addAttribute("chats", chats);
        return "chat";
    }
}
