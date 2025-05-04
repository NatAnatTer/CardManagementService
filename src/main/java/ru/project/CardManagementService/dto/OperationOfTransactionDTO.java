package ru.project.CardManagementService.dto;


public record OperationOfTransactionDTO(String id, String fromCard, String toCard, long dateOfTransfer, long amount, StateOfTransaction state) {
}
