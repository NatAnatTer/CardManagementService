package ru.project.CardManagementService.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.project.CardManagementService.service.OperationOfTransactionService;
import ru.project.CardManagementService.dto.OperationOfTransactionDTO;


/**
 * Card controller - класс для обработки rest запросов для взаимодействия с сущностью,
 * регистрирующей переводы между картами {@class ru.project.CardManagementService.entity.OperationOfTransaction}
 */
@Tag(
        name = "Operation Of Transaction Controller",
        description = "Контроллер для управления переводами"
)
@RequiredArgsConstructor
@RestController
@RequestMapping("/transaction")
public class OperationOfTransactionController {
    private final OperationOfTransactionService service;

    /**
     * Метод возвращает все переводы по идентификатору банковской карты
     *
     * @param pageable параметр пагинации в формате page=0&size=3
     * @param idCard   идентификатор карты
     * @return возвращается постраничный список переводов по карте
     */
    @Operation(summary = "Получение списка переводов по карте",
            description = "Позволяет пользователю получать список переводов с указанной карты. Запрос возвращет постраничный список переводов с указанной картыи")
    @GetMapping
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.OK)
    public Page<OperationOfTransactionDTO> getAllByUserCard(Pageable pageable, @RequestParam @Parameter(description = "Идентификатор карты", required = true, example = "75415afd-f6f8-4abd-b3bd-2b41e4764cd6") String idCard) {
        return service.getAllByCard(pageable, idCard);
    }

    /**
     * Метод выполняет оперцию перевода между картами
     *
     * @param transaction на вход поступает объект с информацией о переводе
     * @return метод возвращает объект выполненного перевода
     */
    @Operation(summary = "Перевод между картами",
            description = "Позволяет пользователю создать транзакцию денежных средств между картами. Запрос принимает на вход JSON с данными о переводе, возвращет информацию о переводе и статус")
    @PostMapping
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.CREATED)
    public OperationOfTransactionDTO createTransaction(@RequestBody OperationOfTransactionDTO transaction) {
        return service.createTransaction(transaction);
    }


}
