package ru.project.CardManagementService.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.project.CardManagementService.Service.PersonService;
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
}
