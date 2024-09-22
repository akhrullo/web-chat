package com.akhrullo.webchat.common;

/**
 * The {@code UserRole} enum defines the different roles available for users
 * in the application, determining their permissions and access levels.
 *
 * <p>Roles include:</p>
 * <ul>
 *     <li>ADMIN - Full access to all resources.</li>
 *     <li>MODERATOR - Limited administrative capabilities.</li>
 *     <li>USER - Standard access to user functionalities.</li>
 * </ul>
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
public enum UserRole {
    ADMIN,
    MODERATOR,
    USER
}
