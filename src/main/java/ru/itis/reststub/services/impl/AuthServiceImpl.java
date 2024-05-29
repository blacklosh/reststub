package ru.itis.reststub.services.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.reststub.dto.request.SignInRequest;
import ru.itis.reststub.dto.request.SignUpRequest;
import ru.itis.reststub.dto.response.BooleanOperationResponse;
import ru.itis.reststub.dto.response.TokenResponse;
import ru.itis.reststub.exceptions.auth.*;
import ru.itis.reststub.models.UserEntity;
import ru.itis.reststub.repositories.UserEntityRepository;
import ru.itis.reststub.services.AuthService;
import ru.itis.reststub.services.CentralAuth;
import ru.itis.reststub.services.CodeGenerationService;
import ru.itis.reststub.services.EmailSenderService;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserEntityRepository userRepository;

    private final EmailSenderService emailSenderService;
    private final CodeGenerationService codeGenerationService;

    private final PasswordEncoder passwordEncoder;

    private static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.HS256;

    @Value("${app.jwt.secret_key}")
    private String secretKey;

    @Value("${app.jwt.access_token.expired_time_in_millis}")
    private long expiredTimeForAccessTokenInMillis;

    @Override
    public BooleanOperationResponse signUp(SignUpRequest request) {
        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyTakenException();
        }
        if(userRepository.findByName(request.getName()).isPresent()) {
            throw new UsernameAlreadyTakenException();
        }
        String confirmCode = codeGenerationService.getConfirmCode();
        UserEntity user = userRepository.save(UserEntity.builder()
                .email(request.getEmail())
                .name(request.getName())
                .hashPassword(passwordEncoder.encode(request.getPassword()))
                .confirmCode(confirmCode)
                .build());
        return new BooleanOperationResponse(emailSenderService.sendConfirmCodeEmail(user.getEmail(), user.getConfirmCode()));
    }

    @Override
    public TokenResponse signUpConfirm(String code) {
        Optional<UserEntity> optionalUser = userRepository.findByConfirmCode(code);
        if(optionalUser.isEmpty()) {
            throw new IncorrectConfirmCodeException();
        }
        UserEntity user = optionalUser.get();
        user.setConfirmCode(null);
        user = userRepository.save(user);
        return createTokens(user);
    }

    @Override
    public TokenResponse signIn(SignInRequest request) {
        Optional<UserEntity> optionalUser = userRepository.findByName(request.getName());
        if(optionalUser.isEmpty()) {
            throw new WrongCredentialsException();
        }
        UserEntity user = optionalUser.get();
        if(!passwordEncoder.matches(request.getPassword(), user.getHashPassword())) {
            throw new WrongCredentialsException();
        }
        return createTokens(user);
    }

    @Override
    public BooleanOperationResponse signOut() {
        log.info("Signing out...");
        UserEntity user = getActiveUser();
        user.setRefreshToken(null);
        return new BooleanOperationResponse(userRepository.save(user).getRefreshToken() == null);
    }

    public UserEntity getActiveUser() {
        return userRepository.findByName(CentralAuth.getCurrentUserName()).orElseThrow(NotLoggedInException::new);
    }

    private TokenResponse createTokens(UserEntity user) {
        return new TokenResponse(generateAccessToken(user.getName()), generateRefreshToken(user));
    }

    private String generateAccessToken(String name) {
        Claims claims = Jwts.claims().setSubject(name);
        Date startTime = new Date();
        Date endTime = new Date(startTime.getTime() + expiredTimeForAccessTokenInMillis);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(startTime)
                .setExpiration(endTime)
                .signWith(ALGORITHM, secretKey)
                .compact();
    }

    private String generateRefreshToken(UserEntity user) {
        String token = UUID.randomUUID().toString();
        user.setRefreshToken(token);
        user = userRepository.save(user);
        return user.getRefreshToken();
    }
}
