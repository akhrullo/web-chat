package com.akhrullo.webchat.user.key;

import com.akhrullo.webchat.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing {@link UserKeys} entities.
 * Provides methods to access and manage user key pairs stored in the database.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
public interface UserKeysRepository extends JpaRepository<UserKeys, Long> {

    /**
     * Finds the {@link UserKeys} associated with a given {@link User}.
     *
     * @param user the user for whom to find the key pair.
     * @return an {@link Optional} containing the {@link UserKeys} if found, or empty if not.
     */
    Optional<UserKeys> findByUser(User user);
}
