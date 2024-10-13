package com.akhrullo.webchat.message;

import com.akhrullo.webchat.chat.Chat;
import com.akhrullo.webchat.chat.ChatService;
import com.akhrullo.webchat.config.SessionContext;
import com.akhrullo.webchat.encryption.EncryptionService;
import com.akhrullo.webchat.exception.WebChatApiException;
import com.akhrullo.webchat.attachment.AttachmentDto;
import com.akhrullo.webchat.attachment.AttachmentService;
import com.akhrullo.webchat.message.dto.CreateMessageDto;
import com.akhrullo.webchat.message.dto.MessageDto;
import com.akhrullo.webchat.message.dto.MessageFileDto;
import com.akhrullo.webchat.message.event.MessageSentEvent;
import com.akhrullo.webchat.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * The {@code MessageServiceImpl} class provides the implementation of the {@code MessageService} interface.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.1
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final ChatService chatService;
    private final MessageMapper messageMapper;
    private final MessageRepository messageRepository;
    private final AttachmentService attachmentService;
    private final ApplicationEventPublisher eventPublisher;
    private final EncryptionService encryptionService;

    @Override
    public MessageDto sendMessage(CreateMessageDto messageDto) {
        User user = SessionContext.getCurrentUser();
        Chat chat = chatService.findChatById(messageDto.getChatId());
        User receiver = getReceiverOfChat(chat.getId(), user);

        if (user.equals(receiver)) {
            log.warn("User {} is trying to send a message to themselves. Ignoring.", user.getId());
            throw WebChatApiException.selfMessageNotAllowed();
        }

        Message message = messageMapper.toEntity(messageDto, chat, user);

        String encryptedContent = encryptionService.encryptForUser(messageDto.getContent(), receiver);
        message.setContent(encryptedContent);

        if (messageDto.getAttachment() != null && !messageDto.getAttachment().isEmpty()) {
            AttachmentDto attachment = attachmentService.saveAttachment(messageDto.getAttachment(), receiver);
            message.setAttachmentId(attachment.getId());
        }

        Message savedMessage = messageRepository.save(message);

        // Trigger the event after the message is saved
        eventPublisher.publishEvent(new MessageSentEvent(this, savedMessage));

        return covertToMessageDto(savedMessage);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MessageDto> getMessagesByChat(Long chatId, Pageable pageable) {
        return messageRepository.findByChatId(chatId, pageable)
                .map(this::covertToMessageDto);
    }

    @Override
    public void markMessagesAsRead(Long chatId) {
        User user = SessionContext.getCurrentUser();
        User receiver = getReceiverOfChat(chatId, user);

        List<Message> messages = messageRepository.findUnreadMessagesByChatIdAndUserId(chatId, receiver.getId());
        messages.forEach(message -> message.setRead(true));
        messageRepository.saveAll(messages);
    }

    @Override
    public MessageDto getMessageById(Long messageId) {
        return messageRepository.findById(messageId)
                .map(this::covertToMessageDto)
                .orElseThrow(WebChatApiException::messageNotFoundException);
    }

    @Override
    public MessageFileDto getAttachmentForMessage(Long messageId) {
        MessageDto messageDto = getMessageById(messageId);
        String path = messageDto.getAttachment().getPath();
        FileSystemResource file = new FileSystemResource(path);

        String contentType;
        try {
            contentType = Files.probeContentType(file.getFile().toPath());
        } catch (IOException ex) {
            // Default content type if detection fails
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        return MessageFileDto.builder()
                .contentType(contentType)
                .filename(file.getFilename())
                .resource(file)
                .build();
    }

    private MessageDto covertToMessageDto(Message message) {
        MessageDto dto = messageMapper.toDto(message);
        User currentUser = SessionContext.getCurrentUser();
        User receiver = getReceiverOfChat(message.getChat().getId(), currentUser);
        boolean isSent = message.getSender().equals(currentUser);
        dto.setSent(isSent);

        if (message.getAttachmentId() != null) {
            AttachmentDto attachment = attachmentService.getAttachment(message.getAttachmentId(), isSent ? receiver : currentUser);
            dto.setAttachment(attachment);
        }

        dto.setContent(encryptionService.decryptForUser(dto.getContent(), isSent ? receiver : currentUser));
        return dto;
    }

    private User getReceiverOfChat(Long chatId, User user) {
        Chat chat = chatService.findChatById(chatId);
        return chat.getUsers().stream()
                .filter(e -> !e.equals(user))
                .findFirst()
                .orElseThrow(WebChatApiException::receiverNotFound);
    }
}
