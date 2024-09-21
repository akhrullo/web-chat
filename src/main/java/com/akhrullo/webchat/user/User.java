package com.akhrullo.webchat.user;

import com.akhrullo.webchat.common.UserRole;
import com.akhrullo.webchat.common.UserState;
import com.akhrullo.webchat.contact.Contact;
import com.akhrullo.webchat.model.audit.AuditingEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class User extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "password")
    private String password;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private UserState state;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @OneToMany(/*mappedBy = "contactOwner",*/
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private Set<Contact> contacts = new HashSet<>();  // Contacts owned by the user
}
