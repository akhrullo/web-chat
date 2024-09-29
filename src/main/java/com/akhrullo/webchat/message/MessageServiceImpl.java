package com.akhrullo.webchat.message;

import com.akhrullo.webchat.chat.Chat;
import com.akhrullo.webchat.chat.ChatService;
import com.akhrullo.webchat.config.SessionContext;
import com.akhrullo.webchat.encryption.MessageEncryptionService;
import com.akhrullo.webchat.exception.WebChatApiException;
import com.akhrullo.webchat.message.dto.CreateMessageDto;
import com.akhrullo.webchat.message.dto.MessageDto;
import com.akhrullo.webchat.message.event.MessageSentEvent;
import com.akhrullo.webchat.user.User;
import com.akhrullo.webchat.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The {@code MessageServiceImpl} class provides the implementation of the {@code MessageService} interface.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final ChatService chatService;
    private final UserService userService;
    private final MessageMapper messageMapper;
    private final MessageRepository messageRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final MessageEncryptionService encryptionService;

    @Override
    public MessageDto sendMessage(CreateMessageDto messageDto) {
        User user = SessionContext.getCurrentUser();
        Chat chat = chatService.findChatById(messageDto.getChatId());
        User receiver = userService.findUserById(messageDto.getReceiverId());

        if (user.getId().equals(messageDto.getReceiverId())) {
            log.warn("User {} is trying to send a message to themselves. Ignoring.", user.getId());
            throw WebChatApiException.selfMessageNotAllowed();
        }

        Message message = messageMapper.toEntity(messageDto, chat, user);
        String encryptMessage = encryptionService.encryptMessageForUser(messageDto.getContent(), receiver);
        message.setContent(encryptMessage);

        Message savedMessage = messageRepository.save(message);

        // Trigger the event after the message is saved
        eventPublisher.publishEvent(new MessageSentEvent(this, savedMessage));

        return messageMapper.toDto(savedMessage);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MessageDto> getMessagesByChat(Long chatId, Pageable pageable) {
        return messageRepository.findByChatId(chatId, pageable)
                .map(message -> {
                    MessageDto dto = messageMapper.toDto(message);
                    dto.setContent(encryptionService.decryptMessageForUser(dto.getContent(), SessionContext.getCurrentUser()));
                    return dto;
                });
    }

    @Override
    public void markMessagesAsRead(Long chatId) {
        User user = SessionContext.getCurrentUser();
        Chat chat = chatService.findChatById(chatId);
        User receiver = chat.getUsers().stream()
                .filter(e -> !e.equals(user))
                .findFirst()
                .orElseThrow(WebChatApiException::receiverNotFound);

        List<Message> messages = messageRepository.findUnreadMessagesByChatIdAndUserId(chatId, receiver.getId());
        messages.forEach(message -> message.setRead(true));
        messageRepository.saveAll(messages);
    }
}
