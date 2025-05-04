package ru.project.CardManagementService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.CardManagementService.entity.UserRole;

import java.util.Collection;
import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    Collection<UserRole> findByUserId(UUID userId);
}
