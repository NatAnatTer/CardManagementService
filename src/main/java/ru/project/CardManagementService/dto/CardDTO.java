package ru.project.CardManagementService.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import ru.project.CardManagementService.entity.StateOfCard;
@Schema(description = "Сущность банковская карта")
public record CardDTO(
        @Schema(description = "Уникальный идентификатор карты", example = "75415afd-f6f8-4abd-b3bd-2b41e4764cd6", accessMode = Schema.AccessMode.READ_ONLY)
        String id,
        @Schema(description = "Номер карты", example = "0000 0000 0000 0000", accessMode = Schema.AccessMode.READ_ONLY)
        String numberOfCard,
        @Schema(description = "Дата окончания срока действия", example = "12332", accessMode = Schema.AccessMode.READ_ONLY)
        long expirationDate,
        @Schema(description = "Статус карты", allowableValues = {"ACTIVE", "BLOCK", "EXPIRED"})
        StateOfCard state,
        @Schema(description = "Баланс карты", example = "1000")
        long balance,
        @Schema(description = "Уникальный идентификатор владельца карты", example = "75415afd-f6f8-4abd-b3bd-2b41e4764cd6", accessMode = Schema.AccessMode.READ_ONLY)
        String personId) {
}
