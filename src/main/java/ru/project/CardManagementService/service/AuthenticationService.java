package ru.project.CardManagementService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.project.CardManagementService.dto.JwtAuthenticationResponse;
import ru.project.CardManagementService.dto.SignInRequest;
import ru.project.CardManagementService.dto.SignUpRequest;
import ru.project.CardManagementService.dto.UserDto;
import ru.project.CardManagementService.exception.NotFoundException;

import java.util.HashSet;

/**
 * Сервис регистрации новых пользователей и аутентификации существующих пользователей
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * Метод регистрации новых пользвоателей в системе
     *
     * @param request на вход принимает объект {@class SignUpRequest}
     * @return возвращает авторизационный токен {@class JwtAuthenticationResponse}
     * @throws NotFoundException - если созданный пользвоатель пользователь не найден
     */
    public JwtAuthenticationResponse signUp(SignUpRequest request) {

        UserDto user = new UserDto();
        user.setLogin(request.getLogin());
        user.setPassword((passwordEncoder.encode(request.getPassword())));
        user.setRoles(new HashSet<>());
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        userService.save(user);

        String jwt = jwtService.generateToken(userService.getUserByName(request.getLogin()).orElseThrow(() -> new NotFoundException("Пользователь не найден")));
        return new JwtAuthenticationResponse(jwt);
    }

    /**
     * Метод аутентификации существующих пользователей
     *
     * @param request на вход принимает объект {@class SignInRequest}
     * @return возвращает авторизационный токен {@class JwtAuthenticationResponse}
     * @throws InternalAuthenticationServiceException - если указанный пользователь не найден
     */
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getLogin(),
                request.getPassword()
        ));

        UserDetails user = userService.loadUserByUsername(request.getLogin());

        String jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }
}
