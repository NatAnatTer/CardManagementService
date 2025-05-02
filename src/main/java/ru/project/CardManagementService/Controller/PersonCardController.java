package ru.project.CardManagementService.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.project.CardManagementService.Service.PersonCardService;
import ru.project.CardManagementService.dto.PersonCardDTO;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/personcard")
public class PersonCardController {
    private final PersonCardService service;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    List<PersonCardDTO> getAll(){
        return service.getAll();
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    PersonCardDTO create(@RequestBody PersonCardDTO personCardDTO){
        return service.savePersonCard(personCardDTO);
    }
}
