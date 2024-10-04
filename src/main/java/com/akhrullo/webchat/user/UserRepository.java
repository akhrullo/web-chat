package com.akhrullo.webchat.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("""
        SELECT u FROM User u
        LEFT JOIN Contact c ON c.user.id = u.id AND c.owner.id = :currentUserId
        WHERE (LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
        AND u.id <> :currentUserId
        AND c.id IS NULL
    """)
    Page<User> findUsersByEmailExcludingCurrentUser(
            @Param("searchTerm") String searchTerm,
            @Param("currentUserId") Long currentUserId,
            Pageable pageable
    );

}
