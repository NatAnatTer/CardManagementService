package ru.project.CardManagementService.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.project.CardManagementService.Service.StateOfCardService;
import ru.project.CardManagementService.dto.StateOfCardDTO;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/statecard")
public class StateOfCardController {
    private final StateOfCardService stateOfCardService;

    @PostMapping("/create")
    public StateOfCardDTO createState(@RequestBody StateOfCardDTO state) {
        return stateOfCardService.createState(state);
    }

    @GetMapping("/all")
    public List<StateOfCardDTO> get() {
        return stateOfCardService.getAll();
    }
}
