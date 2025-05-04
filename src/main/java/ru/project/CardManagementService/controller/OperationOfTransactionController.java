package ru.project.CardManagementService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.project.CardManagementService.service.OperationOfTransactionService;
import ru.project.CardManagementService.dto.OperationOfTransactionDTO;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/transaction")
public class OperationOfTransactionController {
    private final OperationOfTransactionService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OperationOfTransactionDTO> getAll() {
        return service.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OperationOfTransactionDTO createTransaction(@RequestBody OperationOfTransactionDTO transaction) {
        return service.createOperation(transaction);
    }

    @PutMapping
    ResponseEntity<OperationOfTransactionDTO> putOperation(@RequestBody OperationOfTransactionDTO operation) {
        OperationOfTransactionDTO updatedOperation = service.updateTransaction(operation);
        return ResponseEntity.ok(updatedOperation);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {
        service.deleteTransactionById(id);
    }

}
