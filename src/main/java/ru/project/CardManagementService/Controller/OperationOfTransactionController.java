package ru.project.CardManagementService.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.project.CardManagementService.Service.OperationOfTransactionService;
import ru.project.CardManagementService.dto.OperationOfTransactionDTO;
import ru.project.CardManagementService.entity.OperationOfTransaction;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/transaction")
public class OperationOfTransactionController {
    private final OperationOfTransactionService service;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<OperationOfTransactionDTO> getAll(){
      return   service.getAll();
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    private OperationOfTransactionDTO createTransaction(@RequestBody OperationOfTransactionDTO transaction){
        return service.createOperation(transaction);
    }
}
