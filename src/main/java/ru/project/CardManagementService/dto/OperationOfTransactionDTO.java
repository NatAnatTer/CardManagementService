package ru.project.CardManagementService.dto;

import ru.project.CardManagementService.entity.Card;
import ru.project.CardManagementService.entity.StateOfTransaction;


public record OperationOfTransactionDTO(String id, Card fromCard, Card toCard, long dateOfTransfer, long amount, StateOfTransaction state) {
}
