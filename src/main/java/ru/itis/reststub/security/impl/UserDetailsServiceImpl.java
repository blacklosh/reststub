package ru.itis.reststub.security.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.itis.reststub.exceptions.auth.NotConfirmedException;
import ru.itis.reststub.exceptions.auth.NotLoggedInException;
import ru.itis.reststub.models.UserEntity;
import ru.itis.reststub.repositories.UserEntityRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserEntityRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info("Ищем юзверя {}", username);
        UserEntity userAccount = userRepository.findByName(username)
                .orElseThrow(NotLoggedInException::new);

        if(!userAccount.isConfirmed()) {
            throw new NotConfirmedException();
        }

        return User.builder()
                .username(userAccount.getName())
                .password(userAccount.getHashPassword())
                .build();
    }
}
