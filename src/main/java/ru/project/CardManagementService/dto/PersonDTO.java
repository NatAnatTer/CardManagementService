package ru.project.CardManagementService.dto;

import java.util.UUID;

public record PersonDTO(String id, String name, long serialAndNumberOfPassport, long createdAt) {
}
