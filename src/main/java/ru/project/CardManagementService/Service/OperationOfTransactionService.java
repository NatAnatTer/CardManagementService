package ru.project.CardManagementService.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.project.CardManagementService.dto.OperationOfTransactionDTO;
import ru.project.CardManagementService.entity.Card;
import ru.project.CardManagementService.entity.OperationOfTransaction;
import ru.project.CardManagementService.entity.StateOfTransaction;
import ru.project.CardManagementService.mapper.OperationOfTransactionMapper;
import ru.project.CardManagementService.repository.CardRepository;
import ru.project.CardManagementService.repository.OperationOfTransactionRepository;
import ru.project.CardManagementService.repository.StateOfTransactionRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OperationOfTransactionService {

    private final OperationOfTransactionRepository repository;
    private final CardRepository cardRepository;
    private final StateOfTransactionRepository stateRepository;
    private final OperationOfTransactionMapper mapper;

    public List<OperationOfTransactionDTO> getAll() {
        return mapper.toOperationOfTransactionDTOList(repository.findAll());
    }

    public OperationOfTransactionDTO createOperation(OperationOfTransactionDTO operation) {
        Card cardFrom = cardRepository.findById(operation.fromCard().getId())
                .orElseThrow(() -> new IllegalArgumentException("Not found cardFrom with id " + operation.fromCard().getId()));
        Card cardTo = cardRepository.findById(operation.toCard().getId())
                .orElseThrow(() -> new IllegalArgumentException("Not found cardTo with id " + operation.toCard().getId()));
        StateOfTransaction state = stateRepository.findById(operation.state().getId())
                .orElseThrow(() -> new IllegalArgumentException("Not found state with id " + operation.state().getId()));

        OperationOfTransaction operationTransact = repository.save(mapper.toOperationOfTransaction(operation, cardFrom, cardTo, state));

        return mapper.toOperationOfTransactionDTO(operationTransact);
    }

}
