package ru.project.CardManagementService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.CardManagementService.entity.PersonCard;

import java.util.UUID;

public interface PersonCardRepository extends JpaRepository<PersonCard, UUID> {
}
