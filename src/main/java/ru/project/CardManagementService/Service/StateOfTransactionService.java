package ru.project.CardManagementService.Service;

import org.springframework.stereotype.Service;
import ru.project.CardManagementService.dto.StateOfTransactionDTO;
import ru.project.CardManagementService.entity.StateOfTransaction;
import ru.project.CardManagementService.mapper.StateOfTransactionMapper;
import ru.project.CardManagementService.repository.StateOfTransactionRepository;

import java.util.List;
import java.util.UUID;

@Service
public class StateOfTransactionService {
    private final StateOfTransactionRepository repository;
    private final StateOfTransactionMapper mapper;


    public StateOfTransactionService(StateOfTransactionRepository repository, StateOfTransactionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<StateOfTransactionDTO> getAll() {
        return mapper.toStateOfTransactionDTOList(repository.findAll());
    }

    public StateOfTransactionDTO createState(StateOfTransactionDTO stateDTO){
       StateOfTransaction state =  repository.save(mapper.toStateOfTransaction(stateDTO));
        return mapper.toStateOfTransactionDTO(state);
    }
    public void deleteState(String id){
        repository.deleteById(UUID.fromString(id));
    }

    public StateOfTransactionDTO updateState(String id, StateOfTransactionDTO state){
        repository.findById(UUID.fromString(id)).orElseThrow(()-> new IllegalArgumentException("Not found id:"+ id));

       return createState(state);
    }
}
