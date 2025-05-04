package ru.project.CardManagementService.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.project.CardManagementService.dto.OperationOfTransactionDTO;
import ru.project.CardManagementService.entity.Card;
import ru.project.CardManagementService.entity.OperationOfTransaction;
import ru.project.CardManagementService.mapper.OperationOfTransactionMapper;
import ru.project.CardManagementService.repository.OperationOfTransactionRepository;
import ru.project.CardManagementService.repository.StateOfTransactionRepository;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OperationOfTransactionService {

    private final OperationOfTransactionRepository repository;
    private final StateOfTransactionRepository stateRepository;
    private final OperationOfTransactionMapper mapper;

    private final CardService cardService;

    public List<OperationOfTransactionDTO> getAll() {
        return mapper.toOperationOfTransactionDTOList(repository.findAll());
    }
@Transactional
    public OperationOfTransactionDTO createTransaction(OperationOfTransactionDTO operation) {

        synchronized (operation.fromCard().intern()) {
            try {
                UUID id = UUID.fromString(operation.fromCard());
                Card cardFrom = cardService.getCardIfAvailable(id);
                Card cardTo = cardService.getCardIfAvailable(id);
                validationFromCard(id, operation.amount());
                StateOfTransaction state = stateRepository.findById(operation.state().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Not found state with id " + operation.state().getId()));

                OperationOfTransaction operationTransact = repository.save(mapper.toOperationOfTransaction(operation, cardFrom.getId(), cardTo.getId(), state));

                return mapper.toOperationOfTransactionDTO(operationTransact);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }
    }


    public OperationOfTransactionDTO updateTransaction(OperationOfTransactionDTO operation) {
        repository.findById(UUID.fromString(operation.id())).orElseThrow(() -> new IllegalArgumentException("Transaction is not found with id:" + operation.id()));

        return createTransaction(operation);
    }

    public void deleteTransactionById(String id) {
        repository.deleteById(UUID.fromString(id));
    }

    private void validationFromCard(UUID idCard, long amount) {
        Card cardFrom = cardService.getByID(idCard);
        if ((cardFrom.getBalance() - amount) < 0) {
            throw new IllegalArgumentException("Недостаточно средств на карте");
        }
    }


}
