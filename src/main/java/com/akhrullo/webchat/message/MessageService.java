package com.akhrullo.webchat.message;

import com.akhrullo.webchat.message.dto.CreateMessageDto;
import com.akhrullo.webchat.message.dto.MessageDto;
import com.akhrullo.webchat.message.dto.MessageFileDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The {@code MessageService} interface defines methods for managing messages in the chat application.
 * It provides functionality for sending messages, retrieving messages by chat, marking messages as read,
 * and managing message attachments.
 *
 * <p>Methods:</p>
 * <ul>
 *   <li>Send a message with optional attachment.</li>
 *   <li>Get messages by chat ID with pagination.</li>
 *   <li>Mark messages in a chat as read.</li>
 *   <li>Retrieve a message by its ID.</li>
 *   <li>Retrieve an attachment for a message by its ID.</li>
 * </ul>
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.1
 */
public interface MessageService {
    MessageDto sendMessage(CreateMessageDto createMessageDto);

    Page<MessageDto> getMessagesByChat(Long chatId, Pageable pageable);

    void markMessagesAsRead(Long chatId);

    MessageDto getMessageById(Long messageId);

    MessageFileDto getAttachmentForMessage(Long messageId);
}
