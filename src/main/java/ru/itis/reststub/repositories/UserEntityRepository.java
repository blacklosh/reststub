package ru.itis.reststub.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.reststub.models.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserEntityRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByName(String name);

    Optional<UserEntity> findByConfirmCode(String confirmCode);
}
