package com.akhrullo.webchat.message;

import com.akhrullo.webchat.chat.Chat;
import com.akhrullo.webchat.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Class {@code } presents ...
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChat(Chat chat);
}
