package ru.project.CardManagementService.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.project.CardManagementService.service.CardService;
import ru.project.CardManagementService.dto.CardDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(
        name = "Card Controller",
        description = "Контроллер управления картами"
)
@RequiredArgsConstructor
@RestController
@RequestMapping("/card")
public class CardController {
    private final CardService cardService;

    @Operation(summary = "Получение списка карт", description = "Позволяет пользователю администратору получать список всех банковских карт. Запрос возвращет все записи без фильтров и pagination")
    @GetMapping("/all")
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.OK)
    public List<CardDTO> getCard() {
        return cardService.getCards();
    }

    @Operation(summary = "Получение списка карт", description = "Позволяет пользователю получать список банковских карт. Запрос принимает на вход параметры pagination и фильтры, возвращет все записи постранично, с примененными фильтрами")
    @GetMapping
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.OK)
    public Page<CardDTO> getAllCardByFilter(Pageable pageable, @RequestParam @Parameter(description = "Параметры фильтра", example = "state=EXPIRED") Map<String, Object> query) {
        return cardService.getCardsByFilter(pageable, query);
    }

    @Operation(summary = "Запрос баланса", description = "Позволяет пользователю получать баланс указанной карты. Запрос принимает на вход идентификатор карты, возвращет баланс по указанной карте")
    @GetMapping("/balance")
    @SecurityRequirement(name = "JWT")
    public HashMap<Long,String> getBalance(@RequestParam @Parameter(description = "Идентификатор карты",required = true, example = "75415afd-f6f8-4abd-b3bd-2b41e4764cd6") String idCard){
        return cardService.getBalance(idCard);
    }


    @Operation(summary = "Создание банковской карты", description = "Позволяет администратору создавать банковские карты. Запрос принимает на вход json с параметрами карты, возвращет объект созданной карты")
    @PostMapping("/create")
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.CREATED)
    public CardDTO createCard(@RequestBody CardDTO cardDTO) {
        return cardService.saveCard(cardDTO);
    }


    @Operation(summary = "Изменение банковской карты", description = "Позволяет пользователю изменить банковскую карту. Запрос принимает на вход объект  DTO банковской карты, возвращет измененый объект")
    @PutMapping
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<CardDTO> putCard(@RequestBody CardDTO card) {
        CardDTO updatedCard = cardService.updateCard(card);
        return ResponseEntity.ok(updatedCard);
    }


    @Operation(summary = "Блокировка карты", description = "Позволяет пользователю заблокировать банковскую карту. Запрос принимает на вход идентификатор банковской карты, возвращет измененый объект")
    @PutMapping("/blocked")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<CardDTO> blockedCard(@RequestParam @Parameter(description = "Идентификатор карты", required = true, example = "75415afd-f6f8-4abd-b3bd-2b41e4764cd6") String idCard) {
        CardDTO updatedCard = cardService.blockedCard(idCard);
        return ResponseEntity.ok(updatedCard);
    }

    @Operation(summary = "Удаление банковской карты", description = "Позволяет пользователю удалить банковскую карту. Запрос принимает на вход идентификатор банковской карты")
    @DeleteMapping("/delete")
    @SecurityRequirement(name = "JWT")
    public void deleteById(@RequestParam @Parameter(description = "Идентификатор карты", required = true, example = "75415afd-f6f8-4abd-b3bd-2b41e4764cd6") String idCard) {
        cardService.deleteById(idCard);
    }


}
