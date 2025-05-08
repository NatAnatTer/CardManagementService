package ru.project.CardManagementService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на регистрацию")
public class SignUpRequest {

    @Schema(description = "Уникальный идентификатор пользователя", example = "ww2g5vsgdf")
    @Size(min = 5, max = 50, message = "Код пользователя должно содержать от 5 до 50 символов")
    @NotBlank(message = "Имя пользователя не может быть пустыми")
    private String login;

    @Schema(description = "Пароль", example = "0000!!abc")
    @NotBlank(message = "Не может быть пустыми")
    private String password;
    @Schema(description = "ФИО", example = "Иванов Иван Иванович")
    private String name;
    @Schema(description = "Электронная почта", example = "ivan@example.ru")
    private String email;

}
