package ru.project.CardManagementService.dto;

import ru.project.CardManagementService.entity.PersonCard;
import ru.project.CardManagementService.entity.StateOfCardss;


public record CardDTO(String id, PersonCard personCard, StateOfCardss state, long balance) {
}
