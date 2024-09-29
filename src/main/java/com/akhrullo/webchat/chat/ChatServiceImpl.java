package com.akhrullo.webchat.chat;

import com.akhrullo.webchat.chat.dto.ChatDto;
import com.akhrullo.webchat.config.SessionContext;
import com.akhrullo.webchat.exception.WebChatApiException;
import com.akhrullo.webchat.user.User;
import com.akhrullo.webchat.user.UserMapper;
import com.akhrullo.webchat.user.UserService;
import com.akhrullo.webchat.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The {@code ChatServiceImpl} class implements the {@link ChatService} interface,
 * providing business logic for managing chats, including creation, retrieval,
 * and user management within chats.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatMapper mapper;
    private final UserMapper userMapper;
    private final UserService userService;
    private final ChatRepository repository;

    @Override
    public Chat findChatById(Long id) {
        User user = SessionContext.getCurrentUser();
        return repository.findByIdAndUser(id, user)
                .orElseThrow(WebChatApiException::chatNotFoundException);
    }

    @Override
    public ChatDto createChat(Long partnerId) {
        User user = SessionContext.getCurrentUser();
        User partner = userService.findUserById(partnerId);

        if (user.getId().equals(partnerId)) {
            throw WebChatApiException.selfMessageNotAllowed();
        }

        // Check for existing private chat
        Optional<Chat> existingChat = repository.findPrivateChatBetweenUsers(user, partner);
        if (existingChat.isPresent()) {
            return mapper.toDto(existingChat.get());
        }

        Chat chat = mapper.toPrivateChat(user, partner);
        repository.save(chat);

        return mapper.toDto(chat);
    }

    @Override
    public ChatDto getChatById(Long chatId) {
        Chat chat = findChatById(chatId);
        return mapper.toDto(chat);
    }

    @Override
    public List<ChatDto> getAllChats() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getChatUsers(Long chatId) {
        Chat chat = findChatById(chatId);
        return chat.getUsers().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
}
