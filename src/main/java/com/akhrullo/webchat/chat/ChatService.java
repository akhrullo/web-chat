package com.akhrullo.webchat.chat;

import com.akhrullo.webchat.chat.dto.ChatDto;
import com.akhrullo.webchat.user.dto.UserDto;

import java.util.List;

/**
 *  The {@code ChatService} interface defines the contract for chat-related
 * operations, including creating chats, retrieving chat details, and
 * managing chat participants.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
public interface ChatService {
    Chat findChatById(Long id);

    ChatDto createChat(Long partnerId);

    ChatDto getChatById(Long chatId);

    List<ChatDto>  getAllChats();

    List<UserDto> getChatUsers(Long chatId);
}
