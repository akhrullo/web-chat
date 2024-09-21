package com.akhrullo.webchat.config;

import com.akhrullo.webchat.user.User;

/**
 * Class {@code } presents ...
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
public class SessionContext {
    private static final ThreadLocal<User> currentUser = new ThreadLocal<>();
    private static final ThreadLocal<String> currentLanguage = new ThreadLocal<>();

    public static void setCurrentUser(User user) {
        currentUser.set(user);
    }

    public static User getCurrentUser() {
        return currentUser.get();
    }

    public static void setCurrentLanguage(String language) {
        currentLanguage.set(language);
    }

    public static String getCurrentLanguage() {
        return currentLanguage.get();
    }

    public static void clear() {
        currentUser.remove();
        currentLanguage.remove();
    }
}
