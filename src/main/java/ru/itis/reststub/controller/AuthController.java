package ru.itis.reststub.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.itis.reststub.dto.request.SignInRequest;
import ru.itis.reststub.dto.request.SignUpRequest;
import ru.itis.reststub.dto.response.BooleanOperationResponse;
import ru.itis.reststub.dto.response.ErrorResponse;
import ru.itis.reststub.dto.response.TokenResponse;
import ru.itis.reststub.services.AuthService;

@Tag(name = "Контроллер входа", description = "Операции регистрации, входа, выхода")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Начало регистрации", description = "Ввод данных")
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200", description = "Аккаунт создан. Код подтверждения отправлен на почту.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BooleanOperationResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Ошибка в данных. Занят логин. Некорректный пароль",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("/sign-up")
    public BooleanOperationResponse signUp(@RequestBody @Valid SignUpRequest request) {
        return authService.signUp(request);
    }

    @Operation(summary = "Завершение регистрации", description = "Подтверждение почты")
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200", description = "Аккаунт подтверждён.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BooleanOperationResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Ошибка в данных. Неверный код",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PutMapping("/sign-up/{code}")
    public TokenResponse signUpConfirm(@PathVariable String code) {
        return authService.signUpConfirm(code);
    }

    @Operation(summary = "Вход", description = "Ввод данных")
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200", description = "Вход выполнен. Получен токен",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Ошибка в данных. Некорректный логин. Некорректный пароль",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("/sign-in")
    public TokenResponse signIn(@RequestBody @Valid SignInRequest request) {
        return authService.signIn(request);
    }

    @Operation(summary = "Выход")
    @ApiResponses(
            {
                    @ApiResponse(responseCode = "200", description = "Успешно выполнен выход",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BooleanOperationResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Неудачный выход. Некорректный токен",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping("/sign-out")
    public BooleanOperationResponse signOut() {
        return authService.signOut();
    }

}
