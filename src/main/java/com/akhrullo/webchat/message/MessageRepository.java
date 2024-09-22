package com.akhrullo.webchat.message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * The {@code MessageRepository} interface provides methods for accessing and
 * managing {@link Message} entities in the database.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m WHERE m.chat.id = :chatId AND m.user.id = :userId AND m.isRead = false")
    List<Message> findUnreadMessagesByChatIdAndUserId(@Param("chatId") Long chatId, @Param("userId") Long userId);

    Page<Message> findByChatId(Long chatId, Pageable pageable);
}
