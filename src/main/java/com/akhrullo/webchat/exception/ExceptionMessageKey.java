package com.akhrullo.webchat.exception;

import com.akhrullo.webchat.chat.Chat;
import com.akhrullo.webchat.message.Message;
import com.akhrullo.webchat.contact.Contact;
import com.akhrullo.webchat.user.User;
import lombok.experimental.UtilityClass;

/**
 * Utility class {@code ExceptionMessageKey} presents keys by which messages will be taken from properties files.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@UtilityClass
public class ExceptionMessageKey {

    /**
     * Keys for exception messages associated with {@link User}.
     */
    public static final String USER_NOT_FOUND = "user.not.found";
    public static final String RECEIVER_NOT_FOUND = "receiver.not.found";

    /**
     * Keys for exception messages associated with {@link Contact}.
     */
    public static final String CONTACT_NOT_FOUND = "contact.not.found";
    public static final String CONTACT_ALREADY_EXISTS = "contact.already.exists";

    /**
     * Keys for exception messages associated with {@link Chat}.
     */
    public static final String CHAT_NOT_FOUND = "chat.not.found";

    /**
     * Keys for exception messages associated with {@link Message}.
     */
    public static final String SELF_MESSAGE_NOT_ALLOWED = "self.message.not.allowed";
}
