package com.akhrullo.webchat.chat;

import com.akhrullo.webchat.exception.WebChatApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Class {@code } presents ...
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final ChatRepository repository;

    @Override
    public Chat findChatById(Long id) {
        return repository.findById(id)
                .orElseThrow(WebChatApiException::chatNotFoundException);
    }
}
