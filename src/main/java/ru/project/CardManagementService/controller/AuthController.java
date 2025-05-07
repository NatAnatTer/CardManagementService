package ru.project.CardManagementService.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.project.CardManagementService.dto.JwtAuthenticationResponse;
import ru.project.CardManagementService.dto.SignInRequest;
import ru.project.CardManagementService.dto.SignUpRequest;
import ru.project.CardManagementService.service.AuthenticationService;

@Tag(
        name = "Auth Controller",
        description = "Контроллер авторизации/аутенификации"
)
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationService authenticationService;


    @Operation(summary = "Регистрация пользователя", description = "Позволяет создавать новых пользователей.Регистрация пользователя")
    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @Operation(summary = "Авторизация пользователя", description = "Позволяет уже имеющимся пользователям осуществлять вход в систему. Авторизация пользователя")
    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        return authenticationService.signIn(request);
    }



}
