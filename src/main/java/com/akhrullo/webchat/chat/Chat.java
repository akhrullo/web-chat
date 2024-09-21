package com.akhrullo.webchat.chat;

import com.akhrullo.webchat.message.Message;
import com.akhrullo.webchat.model.audit.AuditingEntity;
import com.akhrullo.webchat.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Class {@code } presents ...
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat")
public class Chat extends AuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String image;

    @ManyToMany
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "chat")
    private List<Message> messages = new ArrayList<>();

}
