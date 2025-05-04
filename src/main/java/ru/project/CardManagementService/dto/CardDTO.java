package ru.project.CardManagementService.dto;


import ru.project.CardManagementService.entity.StateOfCard;

public record CardDTO(String id, String numberOfCard, PersonDTO owner, long expirationDate, StateOfCard state, long balance) {
}
