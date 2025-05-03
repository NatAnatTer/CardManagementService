package ru.project.CardManagementService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.project.CardManagementService.service.StateOfTransactionService;
import ru.project.CardManagementService.dto.StateOfTransactionDTO;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("stateoftransaction")
public class StateOfTransactionController {
    private final StateOfTransactionService service;

    @GetMapping("/all")
    public List<StateOfTransactionDTO> getAll() {
        return service.getAll();
    }

    @PostMapping("/create")
    public StateOfTransactionDTO createState(@RequestBody StateOfTransactionDTO state) {
        return service.createState(state);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteState(@PathVariable String id) {
        service.deleteState(id);
    }

    @PutMapping("{id}")
    ResponseEntity<StateOfTransactionDTO> updateState(@PathVariable String id, @RequestBody StateOfTransactionDTO state) {
        StateOfTransactionDTO stateUpdated = service.updateState(id, state);
        return ResponseEntity.ok(stateUpdated);
    }

}

