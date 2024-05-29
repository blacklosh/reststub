package ru.itis.reststub.mappers;

import ru.itis.reststub.dto.response.UserResponse;
import ru.itis.reststub.models.UserEntity;

public interface UserMapper {

    UserResponse toResponse(UserEntity entity);
}
