package com.akhrullo.webchat.message;

import com.akhrullo.webchat.message.dto.CreateMessageDto;
import com.akhrullo.webchat.user.User;

import java.util.List;

/**
 * Class {@code } presents ...
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
public interface MessageService {
    Message sendMessage(User user, Long chatId, CreateMessageDto createMessageDto);

    List<Message> findChatMessages(Long chatId);
}
