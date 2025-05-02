package ru.project.CardManagementService.dto;

import ru.project.CardManagementService.entity.Person;


public record PersonCardDTO(String id, long numberOfCard, Person owner, long expirationDate) {
}
