package com.akhrullo.webchat.message;

import com.akhrullo.webchat.chat.Chat;
import com.akhrullo.webchat.chat.ChatService;
import com.akhrullo.webchat.config.SessionContext;
import com.akhrullo.webchat.exception.WebChatApiException;
import com.akhrullo.webchat.message.dto.CreateMessageDto;
import com.akhrullo.webchat.message.dto.MessageDto;
import com.akhrullo.webchat.message.event.MessageSentEvent;
import com.akhrullo.webchat.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

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
    private final MessageRepository messageRepository;
    private final ChatService chatService;
    private final MessageMapper messageMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public MessageDto sendMessage(CreateMessageDto messageDto) {
        User user = SessionContext.getCurrentUser();
        Chat chat = chatService.findChatById(messageDto.getChatId());

        if (user.getId().equals(messageDto.getReceiverId())) {
            log.warn("User {} is trying to send a message to themselves. Ignoring.", user.getId());
            throw WebChatApiException.selfMessageNotAllowed();
        }

        Message message = messageMapper.toEntity(messageDto, chat, user);

        Message savedMessage = messageRepository.save(message);

        // Trigger the event after the message is saved
        eventPublisher.publishEvent(new MessageSentEvent(this, savedMessage));

        return messageMapper.toDto(savedMessage);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MessageDto> getMessagesByChat(Long chatId, Pageable pageable) {
        return messageRepository.findByChatId(chatId, pageable)
                .map(messageMapper::toDto);
    }

    @Override
    public void markMessagesAsRead(Long chatId) {
        User user = SessionContext.getCurrentUser();
        List<Message> messages = messageRepository.findUnreadMessagesByChatIdAndUserId(chatId, user.getId());
        messages.forEach(message -> message.setRead(true));
        messageRepository.saveAll(messages);
    }
}