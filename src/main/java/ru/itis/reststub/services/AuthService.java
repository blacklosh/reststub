package ru.itis.reststub.services;

import ru.itis.reststub.dto.request.SignInRequest;
import ru.itis.reststub.dto.request.SignUpRequest;
import ru.itis.reststub.dto.response.BooleanOperationResponse;
import ru.itis.reststub.dto.response.TokenResponse;
import ru.itis.reststub.models.UserEntity;

public interface AuthService {

    BooleanOperationResponse signUp(SignUpRequest request);

    TokenResponse signUpConfirm(String code);

    TokenResponse signIn(SignInRequest request);

    BooleanOperationResponse signOut();

    UserEntity getActiveUser();

}
