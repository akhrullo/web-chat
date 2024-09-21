package com.akhrullo.webchat.message;

import com.akhrullo.webchat.message.dto.CreateMessageDto;
import com.akhrullo.webchat.chat.Chat;
import com.akhrullo.webchat.user.User;
import com.akhrullo.webchat.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Class {@code } presents ...
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final ChatService chatService;
    private final MessageRepository repository;

    @Override
    public Message sendMessage(User user, Long chatId, CreateMessageDto createMessageDto) {
        return null;
    }

    @Override
    public List<Message> findChatMessages(Long chatId) {
        Chat chat = chatService.findChatById(chatId);
        return repository.findByChat(chat);
    }
}
