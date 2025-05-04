package ru.project.CardManagementService.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.project.CardManagementService.entity.Card;
import ru.project.CardManagementService.entity.Person;
import ru.project.CardManagementService.entity.StateOfCard;

import java.util.UUID;

public interface CardRepository extends JpaRepository<Card, UUID> {
    Page<Card> findByOwner(Person person, Pageable pageable);
    @Query("select c from Card c where c.numberOfCard like ?1")
    Page<Card> findLikeByNumberOfCard(String numberOfCard, Pageable pageable);
    Page<Card> findByState(StateOfCard state, Pageable pageable);
}
