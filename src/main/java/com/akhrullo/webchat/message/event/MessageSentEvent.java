package com.akhrullo.webchat.message.event;

import com.akhrullo.webchat.message.Message;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * The {@code MessageSentEvent} class represents an event that is published
 * when a message is successfully sent in the chat application.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@Getter
public class MessageSentEvent extends ApplicationEvent {

    private final Message message;

    public MessageSentEvent(Object context, Message message) {
        super(context);
        this.message = message;
    }
}
