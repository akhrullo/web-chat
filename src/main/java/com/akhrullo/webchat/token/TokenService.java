package com.akhrullo.webchat.token;

import com.akhrullo.webchat.user.User;

/**
 * Interface for token-related operations.
 */
public interface TokenService {

    /**
     * Validates whether the JWT token is valid (not expired or revoked).
     *
     * @param jwtToken the JWT token to validate.
     * @return true if the token is valid, false otherwise.
     */
    boolean isTokenValid(String jwtToken);

    /**
     * Saves a new JWT token for the given user.
     *
     * @param user     the user for whom the token is being saved.
     * @param jwtToken the JWT token to be saved.
     */
    void saveUserToken(User user, String jwtToken);

    /**
     * Revokes all valid tokens for the given user by marking them as expired and revoked.
     *
     * @param user the user whose valid tokens need to be revoked.
     */
    void revokeAllUserTokens(User user);

    /**
     * Revokes all valid tokens for the given user and saves a new token in a single operation.
     *
     * @param user     the user for whom tokens need to be revoked and a new token saved.
     * @param jwtToken the new JWT token to be saved after revoking the old tokens.
     */
    void revokeAndSaveNewToken(User user, String jwtToken);
}

