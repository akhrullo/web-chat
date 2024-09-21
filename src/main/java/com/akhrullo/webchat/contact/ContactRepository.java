package com.akhrullo.webchat.contact;

import com.akhrullo.webchat.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Page<Contact> findByOwnerAndNameContainingOrUserEmailContaining(
            User owner,
            String name,
            String email,
            Pageable pageable
    );

    boolean existsByOwnerAndUser(User owner, User user);

    Optional<Contact> findByIdAndOwner(Long id, User owner);
}
