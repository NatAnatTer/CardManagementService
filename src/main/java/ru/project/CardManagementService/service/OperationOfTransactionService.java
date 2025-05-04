package ru.project.CardManagementService.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID id = UUID.fromString(operation.fromCard());
        Card cardFrom = cardService.getCardIfAvailable(id);
        Card cardTo = cardService.getCardIfAvailable(UUID.fromString(operation.toCard()));
        synchronized (operation.fromCard().intern()) {
            try {
                validationFromCard(id, operation.amount());
                cardService.changeAmount(cardFrom, -operation.amount());
                cardService.changeAmount(cardTo, operation.amount());
                OperationOfTransaction transact = mapper.toOperationOfTransaction(operation);
                transact.setState(StateOfTransaction.SUCCESS);
                OperationOfTransaction operationTransact = repository.save(transact);
                return mapper.toOperationOfTransactionDTO(operationTransact);
            } catch (Exception e) {
                OperationOfTransaction transact = mapper.toOperationOfTransaction(operation);
                transact.setState(StateOfTransaction.FAILED);
                repository.save(transact);
                throw new IllegalArgumentException(e);
            }
        }
    }

    private void validationFromCard(UUID idCard, long amount) {
        Card cardFrom = cardService.getByID(idCard);
        if ((cardFrom.getBalance() - amount) < 0) {
            throw new IllegalArgumentException("Недостаточно средств на карте");
        }
    }


}
