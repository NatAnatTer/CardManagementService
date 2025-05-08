package ru.project.CardManagementService.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.project.CardManagementService.service.OperationOfTransactionService;
import ru.project.CardManagementService.dto.OperationOfTransactionDTO;

import java.util.List;
@Tag(
        name = "Operation Of Transaction Controller",
        description = "Контроллер для управления переводами"
)
@RequiredArgsConstructor
@RestController
@RequestMapping("/transaction")
public class OperationOfTransactionController {
    private final OperationOfTransactionService service;

    @Operation(summary = "Получение списка переводов", description = "Позволяет пользователю получать список переводов между картами. Запрос возвращет список переводов между картами")
    @GetMapping
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.OK)
    public List<OperationOfTransactionDTO> getAll() {
        return service.getAll();
    }

    @Operation(summary = "Перевод между картами", description = "Позволяет пользователю создать транзакцию денежных средств между картами. Запрос принимает на вход JSON с данными о переводе, возвращет информацию о переводе и статус")
    @PostMapping
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.CREATED)
    public OperationOfTransactionDTO createTransaction(@RequestBody OperationOfTransactionDTO transaction) {
        return service.createTransaction(transaction);
    }



}
