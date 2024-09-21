package com.akhrullo.webchat.user;

import com.akhrullo.webchat.user.dto.UserDto;
import com.akhrullo.webchat.user.dto.UserUpdateDto;

public interface UserService {

    User findUserById(Long id);

    User findUserByJwt(String jwt);

    UserDto getUserProfile();

    UserDto updateUserProfile(UserUpdateDto updateDto);
}
