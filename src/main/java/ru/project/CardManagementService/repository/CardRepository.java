package ru.project.CardManagementService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.CardManagementService.entity.Card;

import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {
}
