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

@Tag(
        name = "Person Controller",
        description = "Контроллер для управления пользователями"
        )

@RequiredArgsConstructor
@RestController
@RequestMapping("/person")
public class PersonController {
    private final PersonService service;


    @Operation(summary = "Создание пользователя", description = "Позволяет администратору создавать новых пользователей. Запрос принимает на вход объект с информацией о пользователе, возвращет объект созданного пользователя")
    @PostMapping("/create")
    @SecurityRequirement(name = "JWT")
    public PersonDTO create(@RequestBody PersonDTO personDTO) {
        return service.createPerson(personDTO);
    }

    @Operation(summary = "Получение списка пользователей", description = "Позволяет администратору получать список существующих пользователей. Запрос возвращет список пользователей")
    @GetMapping("/all")
    @SecurityRequirement(name = "JWT")
    public List<PersonDTO> get() {
        return service.getAll();
    }

    @Operation(summary = "Удаление пользователя", description = "Позволяет администратору удалять пользователей. Запрос принимает на вход идентификатор пользователя и выполняет удаление записи")
    @DeleteMapping("/delete/{id}")
    @SecurityRequirement(name = "JWT")
    public void deletePerson(@PathVariable String id) {
        service.deletePerson(id);
    }


    @Operation(summary = "Изменение пользователя", description = "Позволяет администратору изменять информацию о пользователе. Запрос принимает на вход объект DTO пользоваеля с измененной информацией, возвращет измененный объект из базы данных")
    @PutMapping
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<PersonDTO> updatePerson(@RequestBody PersonDTO personDTO) {
        PersonDTO personDTO1 = service.updatePerson(personDTO);
        return ResponseEntity.ok(personDTO1);
    }
}
