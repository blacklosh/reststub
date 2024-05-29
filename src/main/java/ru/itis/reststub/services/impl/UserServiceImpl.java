package ru.itis.reststub.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.itis.reststub.dto.response.UserResponse;
import ru.itis.reststub.mappers.UserMapper;
import ru.itis.reststub.services.AuthService;
import ru.itis.reststub.services.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthService authService;

    private final UserMapper userMapper;

    @Override
    public UserResponse getActiveUser() {
        return userMapper.toResponse(authService.getActiveUser());
    }
}
