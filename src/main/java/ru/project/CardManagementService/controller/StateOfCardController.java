package ru.project.CardManagementService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.project.CardManagementService.service.StateOfCardService;
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

    @DeleteMapping("/delete/{id}")
    public void deleteState(@PathVariable String id) {
        stateOfCardService.deleteState(id);
    }

    @PutMapping("/{id}")
    ResponseEntity<StateOfCardDTO> updateState(@PathVariable String id, @RequestBody StateOfCardDTO state) {
        StateOfCardDTO st = stateOfCardService.updateState(id, state);
        return ResponseEntity.ok(st);
    }
}
