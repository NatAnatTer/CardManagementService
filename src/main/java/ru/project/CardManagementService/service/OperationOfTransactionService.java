package ru.project.CardManagementService.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.project.CardManagementService.dto.OperationOfTransactionDTO;
import ru.project.CardManagementService.entity.Card;
import ru.project.CardManagementService.entity.OperationOfTransaction;
import ru.project.CardManagementService.entity.StateOfTransaction;
import ru.project.CardManagementService.mapper.OperationOfTransactionMapper;
import ru.project.CardManagementService.repository.OperationOfTransactionRepository;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OperationOfTransactionService {

    private final OperationOfTransactionRepository repository;
    private final OperationOfTransactionMapper mapper;

    private final CardService cardService;

    public List<OperationOfTransactionDTO> getAll() {
        return mapper.toOperationOfTransactionDTOList(repository.findAll());
    }

    @Transactional
    public OperationOfTransactionDTO createTransaction(OperationOfTransactionDTO operation) {
        UUID id = UUID.fromString(operation.fromCard());
        Card cardFrom = cardService.getCardIfAvailable(id);
        Card cardTo = cardService.getCardIfAvailable(id);
        synchronized (operation.fromCard().intern()) {
            try {
                validationFromCard(id, operation.amount());
                cardService.changeAmount(cardFrom, -operation.amount());
                cardService.changeAmount(cardTo, operation.amount());
                OperationOfTransaction operationTransact = repository.save(mapper.toOperationOfTransaction(operation, cardFrom.getId(), cardTo.getId(), StateOfTransaction.SUCCESS));
                return mapper.toOperationOfTransactionDTO(operationTransact);
            } catch (Exception e) {
                repository.save(mapper.toOperationOfTransaction(operation, cardFrom.getId(), cardTo.getId(), StateOfTransaction.FAILED));
                throw new IllegalArgumentException(e);
            }
        }
    }


//    public OperationOfTransactionDTO updateTransaction(OperationOfTransactionDTO operation) {
//        repository.findById(UUID.fromString(operation.id())).orElseThrow(() -> new IllegalArgumentException("Transaction is not found with id:" + operation.id()));
//
//        return createTransaction(operation);
//    }
//
//    public void deleteTransactionById(String id) {
//        repository.deleteById(UUID.fromString(id));
//    }

    private void validationFromCard(UUID idCard, long amount) {
        Card cardFrom = cardService.getByID(idCard);
        if ((cardFrom.getBalance() - amount) < 0) {
            throw new IllegalArgumentException("Недостаточно средств на карте");
        }
    }


}
