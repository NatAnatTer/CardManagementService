package ru.project.CardManagementService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.project.CardManagementService.service.PersonService;
import ru.project.CardManagementService.dto.PersonDTO;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/person")
public class PersonController {
    private final PersonService service;

    @PostMapping("/create")
    public PersonDTO create(@RequestBody PersonDTO personDTO) {
        return service.createPerson(personDTO);
    }

    @GetMapping("/all")
    public List<PersonDTO> get() {
        return service.getAll();
    }

    @DeleteMapping("/delete/{id}")
    public void deletePerson(@PathVariable String id) {
        service.deletePerson(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonDTO> updatePerson(@PathVariable String id, @RequestBody PersonDTO personDTO) {
        PersonDTO personDTO1 = service.updatePerson(id, personDTO);
        return ResponseEntity.ok(personDTO1);
    }
}
