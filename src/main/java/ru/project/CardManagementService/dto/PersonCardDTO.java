package ru.project.CardManagementService.dto;

import ru.project.CardManagementService.entity.Person;


public record PersonCardDTO(String id, String numberOfCard, Person owner, long expirationDate) {
}
