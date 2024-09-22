package com.akhrullo.webchat.message;

import com.akhrullo.webchat.message.dto.CreateMessageDto;
import com.akhrullo.webchat.message.dto.MessageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The {@code MessageService} interface defines methods for managing messages in the chat application.
 * It provides functionality for sending messages, retrieving messages by chat, and marking messages as read.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
public interface MessageService {
    MessageDto sendMessage(CreateMessageDto createMessageDto);

    Page<MessageDto> getMessagesByChat(Long chatId, Pageable pageable);

    void markMessagesAsRead(Long chatId);
}
