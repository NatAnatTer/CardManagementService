package ru.project.CardManagementService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.CardManagementService.entity.StateOfCardss;

import java.util.UUID;

public interface StateOfCardRepository extends JpaRepository<StateOfCardss, UUID> {
}
