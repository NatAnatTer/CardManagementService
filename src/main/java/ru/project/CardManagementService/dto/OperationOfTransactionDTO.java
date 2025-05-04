package ru.project.CardManagementService.dto;


import ru.project.CardManagementService.entity.StateOfTransaction;

public record OperationOfTransactionDTO(String id, String fromCard, String toCard, long dateOfTransfer, long amount, StateOfTransaction state) {
}
