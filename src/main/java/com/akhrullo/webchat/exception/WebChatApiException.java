package com.akhrullo.webchat.exception;

import static com.akhrullo.webchat.exception.ExceptionMessageKey.*;

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

    public static WebChatApiException receiverNotFound() {
        throw new WebChatApiException(RECEIVER_NOT_FOUND);
    }
}
