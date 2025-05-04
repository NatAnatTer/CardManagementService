package ru.project.CardManagementService.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.project.CardManagementService.dto.UserDto;
import ru.project.CardManagementService.entity.User;
import ru.project.CardManagementService.entity.UserRole;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {

    User toEntity(UserDto entity);
    UserDto toDto(User entity);

    default Set<UserRole> toRoleEntity(Set<String> roles){
        return roles.stream().map(role -> new UserRole()).collect(Collectors.toSet());
    }
    default Set<String> toRole(Set<UserRole> roles){
        return roles.stream().map(UserRole::getRole).collect(Collectors.toSet());
    }
}
