package ru.project.CardManagementService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.CardManagementService.dto.OperationOfTransactionDTO;
import ru.project.CardManagementService.dto.UserDto;
import ru.project.CardManagementService.entity.*;
import ru.project.CardManagementService.exception.IncorrectInputDataException;
import ru.project.CardManagementService.exception.NotFoundException;
import ru.project.CardManagementService.mapper.OperationOfTransactionMapper;
import ru.project.CardManagementService.repository.CardRepository;
import ru.project.CardManagementService.repository.OperationOfTransactionRepository;
import ru.project.CardManagementService.repository.PersonRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис предназначен для реализации бизнес-логики выполнения переводов между картами
 */
@RequiredArgsConstructor
@Service
public class OperationOfTransactionService {

    private final OperationOfTransactionRepository repository;
    private final OperationOfTransactionMapper mapper;
    private final PersonRepository personRepository;
    private final CardRepository cardRepository;

    private final CardService cardService;

    /**
     * Метод получения постранично всех переводов по карте
     * Сначала по идентификатору карты получаем из базы данных объект {@link Card} проверяем что такая карта существует
     * Далее в базе данных запрашиваем по идентификатору карты все переводы с данной карты
     * Полученные данные преобразуем в {@class CardDTO}
     *
     * @param pageable параметр пагинации page=0&size=3 - номер страницы и количество элементов на странице
     * @param idCard   идентификатор карты
     * @return постраничный список переводов с карты
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    public Page<OperationOfTransactionDTO> getAllByCard(Pageable pageable, String idCard) {
        Card cardFrom = cardRepository.findById(UUID.fromString(idCard)).orElseThrow(() -> new NotFoundException("Карта с id =  " + idCard + " не найдена"));
        Page<OperationOfTransaction> listTransaction = repository.findByFromCard(cardFrom.getId(), pageable);
        List<OperationOfTransactionDTO> resultList = listTransaction.getContent().stream().map(this::mapToDTO).toList();

        return new PageImpl<>(resultList, pageable, resultList.size());
    }

    /**
     * Метод создания перевода между картами
     * Сначала выполянется проверка на то, что обе карты существуют и доступны для переводов
     * Затем производится проверка на то, что обе карты принадлежат пользователю
     * Затем проводится проверка на то, что баланс карты, с которой выполняется перевод, больше суммы перевода
     * Далее выполняется изменение суммы на обеих картах и сохранение информации в базе данных
     *
     * @param operation на вход поступает объект {@link OperationOfTransactionDTO}, содержащий информацию для осуществления перевода
     * @return объект, содержащий информацию о выполненном переводе
     * @throws IncorrectInputDataException - возникает если не достаточно средств на карте
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @Transactional
    public OperationOfTransactionDTO createTransaction(OperationOfTransactionDTO operation) {

        UUID id = UUID.fromString(operation.fromCard());
        Card cardFrom = cardService.getCardIfAvailable(UUID.fromString(operation.fromCard()));
        Card cardTo = cardService.getCardIfAvailable(UUID.fromString(operation.toCard()));

        compareUserWithCardUser(cardFrom, cardTo);
        checkIsActiveCard(cardFrom, cardTo);

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
                throw new IncorrectInputDataException("Ошибка. Перевод между картами не выполнен");
            }
        }
    }

    /**
     * Метод предназначен для проверки баланса карты
     * Выполняется сравнение баланса карты и переводимого количества
     *
     * @param idCard идентификатор карты
     * @param amount переводимое количество
     * @throws IncorrectInputDataException - возникает если не достаточно средств на карте
     */
    private void validationFromCard(UUID idCard, long amount) {
        Card cardFrom = cardService.getByID(idCard);
        if ((cardFrom.getBalance() - amount) < 0) {
            throw new IncorrectInputDataException("Недостаточно средств на карте");
        }
    }

    /**
     * Метод проверяет что статус карт с которой переводят и на которую переводят {@class StateOfCard.ACTIVE}
     *
     * @param cardFrom идентификатор карты с которой выполняется перевод
     * @param cardTo   идентификатор карты на которую выполняется перевод
     * @throws IncorrectInputDataException - возникает если какая либо карта не активна
     */
    private void checkIsActiveCard(Card cardFrom, Card cardTo) {
        if (!(cardFrom.getState().equals(StateOfCard.ACTIVE) && cardTo.getState().equals(StateOfCard.ACTIVE))) {
            throw new IncorrectInputDataException("Карта не активна");
        }
    }

    /**
     * Метод сравнения клиента текущего пользователя и владельца карты
     * Сначала получает авторизационный токен, достает из него пользователя и его персональные данные
     * Затем сравнивает {@link Person} пользователя и {@link Person} которому принадлежит карта
     *
     * @param cardFrom карта с которой осуществляется перевод
     * @param cardTo   карта на которую осуществляется перевод
     * @throws NotFoundException           - возникает если персональные данные пользователя не найдены
     * @throws IncorrectInputDataException - возникает если хотя бы одна из карт не принадлежит текущему пользователю
     */
    private void compareUserWithCardUser(Card cardFrom, Card cardTo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = ((UserDto) auth.getPrincipal()).getId();
        Optional<Person> foundPerson = personRepository.findByUserId(UUID.fromString(userId));
        UUID idCurrentPerson;
        if (foundPerson.isPresent()) {
            idCurrentPerson = foundPerson.get().getId();
        } else {
            throw new NotFoundException("Персональные данные пользователя не найдены");
        }

        if (!(idCurrentPerson.equals(cardFrom.getOwner().getId()) && idCurrentPerson.equals(cardTo.getOwner().getId()))) {
            throw new IncorrectInputDataException("Карта не принадлежит текущему пользователю");
        }
    }

    /**
     * Метод преобразует {@link OperationOfTransaction} в {@link OperationOfTransactionDTO}
     *
     * @param operationOfTransaction {@link OperationOfTransaction}
     * @return {@link OperationOfTransactionDTO}
     */
    private OperationOfTransactionDTO mapToDTO(OperationOfTransaction operationOfTransaction) {
        return mapper.toOperationOfTransactionDTO(operationOfTransaction);
    }
}
