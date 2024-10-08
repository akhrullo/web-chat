package com.akhrullo.webchat.auth.oauth;

import com.akhrullo.webchat.user.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * Represents a custom implementation of {@link OAuth2User} that holds user
 * information and attributes from the OAuth2 authentication process..
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@Getter
public class CustomOAuth2User implements OAuth2User {
    private final User user;
    private final Map<String, Object> attributes;

    public CustomOAuth2User(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getName() {
        return user.getEmail();
    }

    public static CustomOAuth2User from(User user, Map<String, Object> attributes) {
        return new CustomOAuth2User(user, attributes);
    }
}
