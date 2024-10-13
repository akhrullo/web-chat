package com.akhrullo.webchat.exception;

import static com.akhrullo.webchat.exception.ExceptionMessageKey.USER_NOT_FOUND;
import static com.akhrullo.webchat.exception.ExceptionMessageKey.CONTACT_ALREADY_EXISTS;
import static com.akhrullo.webchat.exception.ExceptionMessageKey.CONTACT_NOT_FOUND;
import static com.akhrullo.webchat.exception.ExceptionMessageKey.CHAT_NOT_FOUND;
import static com.akhrullo.webchat.exception.ExceptionMessageKey.SELF_MESSAGE_NOT_ALLOWED;
import static com.akhrullo.webchat.exception.ExceptionMessageKey.RECEIVER_NOT_FOUND;
import static com.akhrullo.webchat.exception.ExceptionMessageKey.MESSAGE_NOT_FOUND;
import static com.akhrullo.webchat.exception.ExceptionMessageKey.ATTACHMENT_NOT_FOUND;

/**
 * Custom exception class for the WebChat API, extending {@link RuntimeException}.
 * This class is used to handle specific error scenarios within the application.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
public class WebChatApiException extends RuntimeException {

    public WebChatApiException(String message) {
        super(message);
    }

    public static WebChatApiException userNotFoundException() {
        throw new WebChatApiException(USER_NOT_FOUND);
    }

    public static WebChatApiException contactNotFoundException() {
        throw new WebChatApiException(CONTACT_NOT_FOUND);
    }
    public static WebChatApiException contactAlreadyExists() {
        throw new WebChatApiException(CONTACT_ALREADY_EXISTS);
    }

    public static WebChatApiException chatNotFoundException() {
        throw new WebChatApiException(CHAT_NOT_FOUND);
    }

    public static WebChatApiException selfMessageNotAllowed() {
        throw new WebChatApiException(SELF_MESSAGE_NOT_ALLOWED);
    }

    public static WebChatApiException messageNotFoundException() {
        throw new WebChatApiException(MESSAGE_NOT_FOUND);
    }

    public static WebChatApiException receiverNotFound() {
        throw new WebChatApiException(RECEIVER_NOT_FOUND);
    }

    public static WebChatApiException attachmentNotFound() {
        throw new WebChatApiException(ATTACHMENT_NOT_FOUND);
    }
}
