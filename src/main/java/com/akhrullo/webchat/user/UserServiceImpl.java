package com.akhrullo.webchat.user;

import com.akhrullo.webchat.config.SessionContext;
import com.akhrullo.webchat.user.dto.UserDto;
import com.akhrullo.webchat.user.dto.UserUpdateDto;
import com.akhrullo.webchat.exception.WebChatApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper mapper;
    private final UserRepository repository;


    @Override
    public User findUserById(Long id){
        return repository.findById(id)
                .orElseThrow(WebChatApiException::userNotFoundException);
    }

    @Override
    public User findUserByJwt(String jwt) {
        return repository.findByEmail(jwt)
                .orElseThrow(WebChatApiException::userNotFoundException);
    }

    @Override
    public UserDto getUserProfile() {
        User user = SessionContext.getCurrentUser();
        return mapper.toDto(user);
    }

    @Override
    public UserDto updateUserProfile(UserUpdateDto updateDto) {
        User user = SessionContext.getCurrentUser();

        mapper.updateUserFromDto(updateDto, user);
        repository.save(user);

        return mapper.toDto(user);
    }
}
