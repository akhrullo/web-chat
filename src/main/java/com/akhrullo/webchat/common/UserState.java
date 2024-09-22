package com.akhrullo.webchat.common;

/**
 * The {@code UserState} enum represents the various states a user can have
 * within the application, indicating their current status.
 *
 * <p>States include:</p>
 * <ul>
 *     <li>ACTIVE - The user is currently active and can access the system.</li>
 *     <li>BLOCKED - The user is temporarily blocked from accessing the system.</li>
 *     <li>DELETED - The user account has been permanently deleted.</li>
 * </ul>
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
public enum UserState {
    ACTIVE,
    BLOCKED,
    DELETED
}
