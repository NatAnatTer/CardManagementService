package ru.project.CardManagementService.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import ru.project.CardManagementService.entity.StateOfTransaction;
@Schema(description = "Сущность транзакции")
public record OperationOfTransactionDTO(
        @Schema(description = "Уникальный идентификатор транзакции", example = "75415afd-f6f8-4abd-b3bd-2b41e4764cd6", accessMode = Schema.AccessMode.READ_ONLY)
        String id,
        @Schema(description = "Уникальный идентификатор карты", example = "682b2c26-498a-4c1c-ae86-87b1cdd3a89d", accessMode = Schema.AccessMode.READ_ONLY)
        String fromCard,
        @Schema(description = "Уникальный идентификатор карты", example = "682b2c26-498a-4c1c-ae86-87b1cdd3a89d", accessMode = Schema.AccessMode.READ_ONLY)
        String toCard,
        @Schema(description = "Дата создания транзакции", example = "12332", accessMode = Schema.AccessMode.READ_ONLY)
        long dateOfTransfer,
        @Schema(description = "Переводимое количество", example = "1000")
        long amount,
        @Schema(description = "Статус карты", allowableValues = {"SUCCESS", "FAILED"})
        StateOfTransaction state) {
}
