package ru.project.CardManagementService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
@Schema(description = "Сущность пользователь")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements UserDetails {
    @Schema(description = "Уникальный идентификатор пользователя", example = "7a7e2d9c-f14f-4904-b14c-d07256c7214e", accessMode = Schema.AccessMode.READ_ONLY)
    private String id;
    @Schema(description = "Логин пользователя", example = "Ivan", accessMode = Schema.AccessMode.READ_ONLY)
    private String login;
    @Schema(description = "Пароль", example = "0000!abc")
    private transient String password;
    @Schema(description = "Электронная почта", example = "ivan@example.ru")
    private String email;
    @Schema(description = "ФИО", example = "Иванов Иван Иванович")
    private String name;
    @Schema(description = "Статус карты", allowableValues = {"ROLE_USER", "ROLE_ADMIN"})
    private Set<String> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> (GrantedAuthority) () -> role).toList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }
}
