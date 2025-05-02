package ru.project.CardManagementService.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.project.CardManagementService.Service.StateOfTransactionService;
import ru.project.CardManagementService.dto.StateOfTransactionDTO;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("stateoftransaction")
public class StateOfTransactionController {
    private final StateOfTransactionService service;

    @GetMapping("/all")
    public List<StateOfTransactionDTO> getAll(){
      return  service.getAll();
    }
    @PostMapping("/create")
    public StateOfTransactionDTO createState(@RequestBody StateOfTransactionDTO state){
       return service.createState(state);
    }

}

