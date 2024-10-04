package com.akhrullo.webchat.user;

import com.akhrullo.webchat.user.dto.UserDto;
import com.akhrullo.webchat.user.dto.UserUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    User findUserById(Long id);

    UserDto getUserProfile();

    UserDto updateUserProfile(UserUpdateDto updateDto);

    Page<UserDto> searchUsers(String searchTerm, Pageable pageable);
}
