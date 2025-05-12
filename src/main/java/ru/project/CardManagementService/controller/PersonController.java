package ru.project.CardManagementService.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.project.CardManagementService.service.PersonService;
import ru.project.CardManagementService.dto.PersonDTO;

import java.util.List;

/**
 * PersonController - класс для обработки rest запросов для взаимодействия с сущностью,
 * содержащей персональные данные клиента {@class ru.project.CardManagementService.entity.Person}
 */
@Tag(
        name = "Person Controller",
        description = "Контроллер для управления персональными данными клиента"
)

@RequiredArgsConstructor
@RestController
@RequestMapping("/person")
public class PersonController {
    private final PersonService service;

    /**
     * Метод создания записи с персональными данными о клиенте
     *
     * @param personDTO на вход поступает объект с заполненными персональными данными
     * @return <Code>(PersonDTO)</Code> метод возвращает созданный объект
     */
    @Operation(summary = "Создание клиента",
            description = "Позволяет администратору создавать новых клиентов. Запрос принимает на вход объект с информацией о клиенте, возвращет объект созданного клиента")
    @PostMapping("/create")
    @SecurityRequirement(name = "JWT")
    public PersonDTO create(@RequestBody PersonDTO personDTO) {
        return service.createPerson(personDTO);
    }

    /**
     * Метод для получения списка всех клиентов
     *
     * @return <Code>(List<PersonDTO>)</Code> возвращает список всех клиентов
     */
    @Operation(summary = "Получение списка клиентов",
            description = "Позволяет администратору получать список существующих клиентов. Запрос возвращет список клиентов")
    @GetMapping("/all")
    @SecurityRequirement(name = "JWT")
    public List<PersonDTO> get() {
        return service.getAll();
    }

    /**
     * Метод удаления клиента
     *
     * @param id на вход получает идентификатор клиента
     */
    @Operation(summary = "Удаление клиента",
            description = "Позволяет администратору удалять клиентов. Запрос принимает на вход идентификатор клиента и выполняет удаление записи")
    @DeleteMapping("/delete/{id}")
    @SecurityRequirement(name = "JWT")
    public void deletePerson(@PathVariable String id) {
        service.deletePerson(id);
    }

    /**
     * Метод изменения информации о клиенте
     *
     * @param personDTO на вход поступает объект <Code>(PersonDTO)</Code> с измененными полями
     * @return метод возвращает измененный объект
     */
    @Operation(summary = "Изменение клиента",
            description = "Позволяет администратору изменять информацию о клиенте. Запрос принимает на вход объект DTO клиента с измененной информацией, возвращет измененный объект из базы данных")
    @PutMapping
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<PersonDTO> updatePerson(@RequestBody PersonDTO personDTO) {
        PersonDTO personDTO1 = service.updatePerson(personDTO);
        return ResponseEntity.ok(personDTO1);
    }
}
