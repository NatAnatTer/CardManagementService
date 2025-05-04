package ru.project.CardManagementService.dto;


import ru.project.CardManagementService.entity.Person;
import ru.project.CardManagementService.entity.StateOfCard;

public record CardDTO(String id, String numberOfCard, Person owner, long expirationDate, StateOfCard state, long balance) {
}
