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
import java.util.Map;

/**
 * Card controller - класс для обработки rest запросов для взаимодействия с сущностью банковская карта {@class Card}
 */
@Tag(
        name = "Card Controller",
        description = "Контроллер управления картами"
)
@RequiredArgsConstructor
@RestController
@RequestMapping("/card")
public class CardController {
    private final CardService cardService;

    /**
     * Метод для вывода списка всех банковских карт
     *
     * @param pageable принимает на вход параметры пагинации page=0&size=3 - номер страницы и количество элементов на странице
     * @return <Code>(Page<CardDTO>)</Code> возвразает список банковских карт в формате DTO с постраничным выводом
     */
    @Operation(summary = "Получение списка карт", description = "Позволяет пользователю администратору получать список всех банковских карт. Запрос возвращет все записи без фильтров и pagination")
    @GetMapping("/all")
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.OK)
    public Page<CardDTO> getCard(Pageable pageable) {
        return cardService.getCards(pageable);
    }

    /**
     * Метод получения фильтрованного списка карт
     *
     * @param pageable параметры пагинации <Code>(page=0&size=3)</Code> - номер страницы и количество элементов на странице
     * @param query    который представляет из себя <Code>(Map<String, Object>)</Code> и содержит наименование поля, по которому производится фильтрация и значение фильтра
     * @return <Code>(Page<CardDTO>)</Code> возвразает список банковских карт в формате DTO согласно указанным в запросе номеру страницы и количеством элеменотв на странице
     */
    @Operation(summary = "Получение списка карт", description = "Позволяет пользователю получать список банковских карт. Запрос принимает на вход параметры pagination и фильтры, возвращет все записи постранично, с примененными фильтрами")
    @GetMapping
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.OK)
    public Page<CardDTO> getAllCardByFilter(Pageable pageable, @RequestParam @Parameter(description = "Параметры фильтра", example = "state=EXPIRED") Map<String, Object> query) {
        return cardService.getCardsByFilter(pageable, query);
    }

    /**
     * Метод предназначен для получения баланса карты
     *
     * @param idCard на вход получает идентификатор карты, по корой необходимо получить баланс
     * @return <Code>(HashMap<Long,String>)</Code> метод возвращает пару значений - баланс и номер карты
     */
    @Operation(summary = "Запрос баланса", description = "Позволяет пользователю получать баланс указанной карты. Запрос принимает на вход идентификатор карты, возвращет баланс по указанной карте")
    @GetMapping("/balance")
    @SecurityRequirement(name = "JWT")
    public HashMap<Long, String> getBalance(@RequestParam @Parameter(description = "Идентификатор карты", required = true, example = "75415afd-f6f8-4abd-b3bd-2b41e4764cd6") String idCard) {
        return cardService.getBalance(idCard);
    }

    /**
     * Метод создания банковской карты
     *
     * @param cardDTO на вход подается объект с реквизитами карты
     * @return возвращает метод возвращает созданный объект с присвоенным уникальным идентификатором
     */
    @Operation(summary = "Создание банковской карты", description = "Позволяет администратору создавать банковские карты. Запрос принимает на вход json с параметрами карты, возвращет объект созданной карты")
    @PostMapping("/create")
    @SecurityRequirement(name = "JWT")
    @ResponseStatus(HttpStatus.CREATED)
    public CardDTO createCard(@RequestBody CardDTO cardDTO) {
        return cardService.saveCard(cardDTO);
    }

    /**
     * Метод изменения банковской карты
     *
     * @param card на вход подается обхект карты с измененными полями
     * @return возвращает метод обновленный объект
     */
    @Operation(summary = "Изменение банковской карты", description = "Позволяет пользователю изменить банковскую карту. Запрос принимает на вход объект  DTO банковской карты, возвращет измененый объект")
    @PutMapping
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<CardDTO> putCard(@RequestBody CardDTO card) {
        CardDTO updatedCard = cardService.updateCard(card);
        return ResponseEntity.ok(updatedCard);
    }

    /**
     * Метеод блокировки банковской карты
     *
     * @param idCard на вход поступает идентификатор карты
     * Метод меняет статус карты
     * @return возвращает метод измененный объект карты
     */
    @Operation(summary = "Блокировка карты", description = "Позволяет пользователю заблокировать банковскую карту. Запрос принимает на вход идентификатор банковской карты, возвращет измененый объект")
    @PutMapping("/blocked")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<CardDTO> blockedCard(@RequestParam @Parameter(description = "Идентификатор карты", required = true, example = "75415afd-f6f8-4abd-b3bd-2b41e4764cd6") String idCard) {
        CardDTO updatedCard = cardService.blockedCard(idCard);
        return ResponseEntity.ok(updatedCard);
    }

    /**
     * Метод удаления банковской карты
     *
     * @param idCard на вход поступает идентификатор карты
     * Метод удаляет банковскую карту и ни чего клиенту не возвращает
     */
    @Operation(summary = "Удаление банковской карты", description = "Позволяет пользователю удалить банковскую карту. Запрос принимает на вход идентификатор банковской карты")
    @DeleteMapping("/delete")
    @SecurityRequirement(name = "JWT")
    public void deleteById(@RequestParam @Parameter(description = "Идентификатор карты", required = true, example = "75415afd-f6f8-4abd-b3bd-2b41e4764cd6") String idCard) {
        cardService.deleteById(idCard);
    }


}
