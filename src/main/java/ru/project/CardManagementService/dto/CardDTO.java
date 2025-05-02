package ru.project.CardManagementService.dto;

import ru.project.CardManagementService.entity.PersonCard;
import ru.project.CardManagementService.entity.StateOfCard;


public record CardDTO(String id, PersonCard personCard, StateOfCard state, long balance) {
}
