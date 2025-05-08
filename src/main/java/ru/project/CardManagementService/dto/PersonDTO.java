package ru.project.CardManagementService.dto;


import io.swagger.v3.oas.annotations.media.Schema;
@Schema(description = "Сущность person")
public record PersonDTO(
        @Schema(description = "Уникальный идентификатор person", example = "75415afd-f6f8-4abd-b3bd-2b41e4764cd6", accessMode = Schema.AccessMode.READ_ONLY)
        String id,
        @Schema(description = "ФИО", example = "Иванов Иван Иванович")
        String name,
        @Schema(description = "серия и номер паспорта", example = "00 00 000000")
        long serialAndNumberOfPassport,
        @Schema(description = "Дата создания записи", example = "12332", accessMode = Schema.AccessMode.READ_ONLY)
        long createdAt,
        @Schema(description = "Уникальный идентификатор пользователя", example = "75415afd-f6f8-4abd-b3bd-2b41e4764cd6", accessMode = Schema.AccessMode.READ_ONLY)
        String userId) {
}
