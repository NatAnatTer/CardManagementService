package ru.project.CardManagementService.dto;


public record CardDTO(String id, PersonCard personCard, StateOfCardss state, long balance) {
}
