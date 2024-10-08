package com.akhrullo.webchat.auth.oauth;

import com.akhrullo.webchat.common.UserRole;
import com.akhrullo.webchat.common.UserState;
import com.akhrullo.webchat.encryption.keymanagement.KeyManagementService;
import com.akhrullo.webchat.user.User;
import com.akhrullo.webchat.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service that handles the loading and processing of OAuth2 user details
 * from an OAuth2 authentication request.
 * This service integrates with the user repository to either register new users
 * or update existing user information based on their OAuth2 account details.
 * It implements the {@link OAuth2UserService} interface to provide
 * functionality for managing OAuth2 users.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final KeyManagementService keyManagementService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        User user = processOAuth2User(oauth2User);

        return CustomOAuth2User.from(user, oauth2User.getAttributes());
    }

    private User processOAuth2User(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        String firstname = oAuth2User.getAttribute("given_name");
        String lastname = oAuth2User.getAttribute("family_name");

        return userRepository.findByEmail(email)
                .map(existingUser -> updateUser(existingUser, firstname, lastname))
                .orElseGet(() -> registerNewUser(email, firstname, lastname));
    }

    private User updateUser(User existingUser, String firstname, String lastname) {
        // Update any user details if necessary (optional)
        existingUser.setFirstname(firstname);
        existingUser.setLastname(lastname);
        return userRepository.save(existingUser);
    }

    // TODO extract to UserService
    private User registerNewUser(String email, String firstname, String lastname) {
        User user = User.builder()
                .firstname(firstname)
                .lastname(lastname)
                .email(email)
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .role(UserRole.USER)
                .state(UserState.ACTIVE)
                .build();

        User savedUser = userRepository.save(user);
        keyManagementService.generateAndStoreKeysForUser(savedUser);

        return savedUser;
    }
}
