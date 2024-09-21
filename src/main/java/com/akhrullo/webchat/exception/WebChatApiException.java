package com.akhrullo.webchat.exception;

import static com.akhrullo.webchat.exception.ExceptionMessageKey.*;

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
}
