package ru.project.CardManagementService.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.CardManagementService.entity.Card;
import ru.project.CardManagementService.entity.Person;
import ru.project.CardManagementService.entity.StateOfCard;

import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {
    Page<Card> findByOwner(Person person, Pageable pageable);

    Page<Card> findByOwnerAndState(Person person, StateOfCard state, Pageable pageable);

    Page<Card> findByState(StateOfCard state, Pageable pageable);
}
