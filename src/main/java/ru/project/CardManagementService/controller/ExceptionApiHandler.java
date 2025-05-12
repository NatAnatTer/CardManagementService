package ru.project.CardManagementService.controller;

import org.springdoc.api.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.project.CardManagementService.exception.IncorrectInputDataException;
import ru.project.CardManagementService.exception.NotFoundException;

/**
 * ExceptionApiHandler - класс, для обработки исключений, возникающих в процессе работы приложения
 */
@RestControllerAdvice
public class ExceptionApiHandler {
    /**
     * Метод обработки ошибки клиета 404 - запрошенный ресурс не найден
     *
     * @param exception на вход передается ошибка {@class ru.project.CardManagementService.exception.NotFoundException}
     * @return возвращается <Code>(ResponseEntity<ErrorMessage>)</Code> код ошибки <Code>(HttpStatus.NOT_FOUND)</Code> и сообщение об ошибке
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> notFoundException(NotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage()));
    }

    /**
     * Метод обработки ошибки клиента 400 - не верный формат входных данных
     *
     * @param exception на вход передается ошибка {@class IncorrectInputDataException}
     * @return возвращается <Code>(ResponseEntity<ErrorMessage>)</Code> код ошибки <Code>(HttpStatus.BAD_REQUEST)</Code> и сообщение об ошибке
     */
    @ExceptionHandler(IncorrectInputDataException.class)
    public ResponseEntity<ErrorMessage> IncorrectInputDataException(IncorrectInputDataException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(exception.getMessage()));
    }

    /**
     * Метод обработки ошибки отсутствия прав доступа
     *
     * @param exception на вход передается ошибка {@class AuthorizationDeniedException}
     * @return возвращается <Code>(ResponseEntity<ErrorMessage>)</Code> код ошибки <Code>(HttpStatus.INTERNAL_SERVER_ERROR)</Code> и сообщение об ошибке
     */
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorMessage> AuthorizationDeniedException(AuthorizationDeniedException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessage("Недостаточно прав для выполнения операции " + exception.getMessage()));
    }

    /**
     * Метод обработки ошибки формата переданных данных
     *
     * @param exception на вход передается ошибка {@class HttpMessageNotReadableException}
     * @return возвращается <Code>(ResponseEntity<ErrorMessage>)</Code> код ошибки <Code>(HttpStatus.INTERNAL_SERVER_ERROR)</Code> и сообщение об ошибке
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessage> HttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessage("Ошибка формата переданных данных"));
    }

    /**
     * Метод обработки ошибки аутентификации
     *
     * @param exception на вход передается ошибка {@class InternalAuthenticationServiceException}
     * @return возвращается <Code>(ResponseEntity<ErrorMessage>)</Code> код ошибки <Code>(HttpStatus.BAD_REQUEST)</Code> и сообщение об ошибке
     */
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ErrorMessage> InternalAuthenticationServiceException(InternalAuthenticationServiceException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(exception.getMessage()));
    }

    /**
     * Метод обработки ошибки авторизации, связанной с вводом неверного пароля
     *
     * @param exception на вход передается ошибка {@class BadCredentialsException}
     * @return возвращается <Code>(ResponseEntity<ErrorMessage>)</Code> код ошибки <Code>(HttpStatus.UNAUTHORIZED)</Code> и сообщение об ошибке
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorMessage> BadCredentialsException(BadCredentialsException exception) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorMessage("Не верный пароль"));
    }

    /**
     * Метод предназначен для обработки всех остальных ошибок
     *
     * @param exception на вход передается ошибка {@class Throwable}
     * @return возвращается <Code>(ResponseEntity<ErrorMessage>)</Code> код ошибки <Code>(HttpStatus.INTERNAL_SERVER_ERROR)</Code> и сообщение об ошибке
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorMessage> DifferentGeneralException(Throwable exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessage("Ошибка сервиса " + exception.getMessage()));
    }

}
