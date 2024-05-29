package ru.itis.reststub.mappers.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.itis.reststub.dto.response.UserResponse;
import ru.itis.reststub.mappers.UserMapper;
import ru.itis.reststub.models.UserEntity;

@Slf4j
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserResponse toResponse(UserEntity entity) {
        return UserResponse.builder()
                .email(entity.getEmail())
                .name(entity.getName())
                .isConfirmed(entity.isConfirmed())
                .build();
    }
}
