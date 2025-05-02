package ru.project.CardManagementService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.CardManagementService.entity.StateOfCard;

import java.util.UUID;

public interface StateOfCardRepository extends JpaRepository<StateOfCard, UUID> {
}
