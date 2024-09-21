package com.akhrullo.webchat.user;

import com.akhrullo.webchat.user.dto.UserDto;
import com.akhrullo.webchat.user.dto.UserUpdateDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Class {@code UserController} handles HTTP requests related to user management.
 * It provides endpoints for retrieving and updating user profiles.
 * This controller uses the {@link UserService} to perform operations on users.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    /**
     * Retrieves the profile of a user by ID.
     *
     * @return ResponseEntity with the UserDto and HTTP status 200 OK
     */
    @GetMapping("/profile")
    public ResponseEntity<UserDto> getUserProfile() {
        UserDto userDto = userService.getUserProfile();
        return ResponseEntity.ok(userDto);
    }

    /**
     * Updates the profile of a user by ID.
     *
     * @param updateDto the DTO containing updated user details
     * @return ResponseEntity with the updated UserDto and HTTP status 200 OK
     */
    @PutMapping("/profile")
    public ResponseEntity<UserDto> updateUserProfile(
            @Valid @RequestBody UserUpdateDto updateDto
    ) {
        UserDto userDto = userService.updateUserProfile(updateDto);
        return ResponseEntity.ok(userDto);
    }
}

