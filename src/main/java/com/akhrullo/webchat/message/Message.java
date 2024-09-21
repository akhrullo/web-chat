package com.akhrullo.webchat.message;

import com.akhrullo.webchat.chat.Chat;
import com.akhrullo.webchat.model.audit.AuditingEntity;
import com.akhrullo.webchat.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Class {@code } presents ...
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@Getter
@Setter
@Entity
@Table(name = "message")
public class Message extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String content;
    private String image;

    @ManyToOne
    private User user;

    @ManyToOne
    private Chat chat;

    @Version
    private Long version;
}
