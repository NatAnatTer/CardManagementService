package ru.project.CardManagementService.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.project.CardManagementService.dto.OperationOfTransactionDTO;
import ru.project.CardManagementService.dto.UserDto;
import ru.project.CardManagementService.entity.*;
import ru.project.CardManagementService.mapper.OperationOfTransactionMapper;
import ru.project.CardManagementService.repository.OperationOfTransactionRepository;
import ru.project.CardManagementService.repository.PersonRepository;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OperationOfTransactionService {

    private final OperationOfTransactionRepository repository;
    private final OperationOfTransactionMapper mapper;
    private final PersonRepository personRepository;

    private final CardService cardService;
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<OperationOfTransactionDTO> getAll() {
        return mapper.toOperationOfTransactionDTOList(repository.findAll());
    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @Transactional
    public OperationOfTransactionDTO createTransaction(OperationOfTransactionDTO operation) {

        UUID id = UUID.fromString(operation.fromCard());
        Card cardFrom = cardService.getCardIfAvailable(UUID.fromString(operation.fromCard()));
        Card cardTo = cardService.getCardIfAvailable(UUID.fromString(operation.toCard()));
        if(!compareUserWithCardUser(cardFrom, cardTo)){
            return null;
        }
        if(!checkIsActiveCard(cardFrom, cardTo)){
            return null;
        }
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
    private boolean checkIsActiveCard(Card cardFrom, Card cardTo){
      //  StateOfCard stateCardFrom = cardService.getCardIfAvailable(idCardFrom).getState();
     //   StateOfCard stateCardTo = cardService.getCardIfAvailable(idCardTo).getState();
        return cardFrom.getState().equals(StateOfCard.ACTIVE)&&cardTo.getState().equals(StateOfCard.ACTIVE);
    }
    private boolean compareUserWithCardUser(Card cardFrom, Card cardTo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = ((UserDto) auth.getPrincipal()).getId();
        UUID idCurrentPerson = personRepository.findByUserId(UUID.fromString(userId)).get().getId();
    //    UUID personCardFrom = cardService.getCardIfAvailable(idCardFrom).getOwner().getId();
    //    UUID personCardTo = cardService.getCardIfAvailable(idCardTo).getOwner().getId();
        return idCurrentPerson.equals(cardFrom.getOwner().getId())&&idCurrentPerson.equals(cardTo.getOwner().getId());


    }

}
