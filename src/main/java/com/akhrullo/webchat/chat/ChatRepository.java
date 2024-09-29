package com.akhrullo.webchat.chat;

import com.akhrullo.webchat.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * The {@code ChatRepository} interface provides methods for accessing and
 * managing {@link Chat} entities in the database.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query("SELECT c FROM Chat c WHERE c.type = 'PRIVATE' AND :user1 MEMBER OF c.users AND :user2 MEMBER OF c.users")
    Optional<Chat> findPrivateChatBetweenUsers(@Param("user1") User user1, @Param("user2") User user2);

    @Query("SELECT c FROM Chat c WHERE c.id = :chatId AND :user MEMBER OF c.users")
    Optional<Chat> findByIdAndUser(@Param("chatId") Long chatId, @Param("user") User user);
}
