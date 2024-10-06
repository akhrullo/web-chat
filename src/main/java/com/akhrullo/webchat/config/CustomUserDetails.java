package com.akhrullo.webchat.config;

import com.akhrullo.webchat.common.UserState;
import com.akhrullo.webchat.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Represents the details of a user for Spring Security authentication.
 *
 * <p>Provides methods to access user information and authorities.</p>
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
public record CustomUserDetails(User user) implements UserDetails {
    public static CustomUserDetails from(User user) {
        return new CustomUserDetails(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();  // Use email as username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getState() != UserState.BLOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getState() == UserState.ACTIVE;
    }
}

