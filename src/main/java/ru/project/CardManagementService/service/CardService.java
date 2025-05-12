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
import ru.project.CardManagementService.dto.CardDTO;
import ru.project.CardManagementService.dto.UserDto;
import ru.project.CardManagementService.entity.Card;
import ru.project.CardManagementService.entity.Person;
import ru.project.CardManagementService.entity.StateOfCard;
import ru.project.CardManagementService.exception.IncorrectInputDataException;
import ru.project.CardManagementService.exception.NotFoundException;
import ru.project.CardManagementService.mapper.CardMapper;
import ru.project.CardManagementService.repository.CardRepository;
import ru.project.CardManagementService.repository.PersonRepository;
import ru.project.CardManagementService.security.CryptoService;

import java.util.*;

/**
 * Card service - предназначен для реализации бизнес-логики выполнения CRUD-операций с субъектом банковская карта
 * Card entity located  by link {@link ru.project.CardManagementService.entity.Card}
 */
@RequiredArgsConstructor
@Service
public class CardService {
    private final CardRepository cardRepository;
    private final PersonRepository personRepository;
    private final CardMapper mapper;
    private final CryptoService cryptoService;

    /**
     * Метод получения постраничного списка банковских карт
     *
     * @param pageable на вход поступает параметры пагинации page=0&size=3 - номер страницы и количество элементов на странице
     * @return <Code>(Page<CardDTO>)</Code> возвразает список банковских карт в формате DTO с постраничным выводом
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Page<CardDTO> getCards(Pageable pageable) {
        Page<Card> listCards = cardRepository.findAll(pageable);
        List<CardDTO> cardDTO = listCards.getContent().stream().map(this::map).toList();
        return new PageImpl<>(cardDTO, pageable, cardDTO.size());
    }

    /**
     * Метод получения постраничного фильтрованного списка банковских карт
     * Выполняется проверка, если запрос query пустой, выполняется проверка,
     * если пользователь с ролью {@class Role.ROLE_ADMIN}
     * выполняется постраничное получение всех банковских карт
     * если пользователь с ролью {@class Role.ROLE_USER}
     * выполняется постраничное получение банковских карт текущего пользователя
     * возвращается полученный результат в формате {@link CardDTO}
     * Если запрос query не пустой производится его разбор
     * Считывается параметр, отвечающий за наименование поля,
     * согласно полученных данных выполняется вызов фильтров и преобразования
     * возвращается полученный результат в формате {@link CardDTO}
     *
     * @param pageable параметр пагинации page=0&size=3 - номер страницы и количество элементов на странице
     * @param query    параметр фильтра <Code>(Map<String, Object>)</Code>
     *                 где String - это наименование поля, по которому выполняется фильтрация, Object - значение фильтра
     * @return <Code>(Page<CardDTO>)</Code> возвразает список банковских карт в формате DTO с постраничным выводом
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public Page<CardDTO> getCardsByFilter(Pageable pageable, Map<String, Object> query) {
        List<CardDTO> listCardDto;
        if (query.size() > 2) {//(!query.isEmpty()) {

            Page<Card> result = null;
            for (String field : query.keySet()) {
                String value = String.valueOf(query.get(field));
                switch (field) {
                    case ("id"):
                        if (getRoleFromAuthority().equals("ROLE_ADMIN") || getPersonFromAuthority().getId().equals(UUID.fromString(value))) {
                            result = getByOwnerId(value, pageable);
                        } else {
                            result = null;
                        }
                        break;
                    case ("state"):
                        if (getRoleFromAuthority().equals("ROLE_ADMIN")) {
                            StateOfCard state = StateOfCard.valueOf(value);
                            result = cardRepository.findByState(state, pageable);
                        } else {
                            result = getByOwnerIdAndState(getPersonFromAuthority().getId().toString(), StateOfCard.valueOf(value), pageable);
                        }
                        break;
                    default:
                        continue;
                }
            }
            if (Objects.nonNull(result)) {
                List<CardDTO> filteredCardDTO = result.getContent().stream().map(this::map).toList();
                return new PageImpl<>(filteredCardDTO, pageable, filteredCardDTO.size());
            } else {
                return null;
            }
        }
        if (getRoleFromAuthority().equals("ROLE_ADMIN")) {
            Page<Card> card = cardRepository.findAll(pageable);
            listCardDto = card.getContent().stream().map(this::map).toList();
        } else {
            Page<Card> result = getByOwnerId(getPersonFromAuthority().getId().toString(), pageable);
            listCardDto = result.getContent().stream().map(this::map).toList();
        }
        return new PageImpl<>(listCardDto, pageable, listCardDto.size());
    }

    /**
     * Метод получения баланса определенной карты
     * Сначала выполняется получение данных о клиенте из авторизационного токена,
     * Затем производится поиск карты по идентификатору
     * Затем производится проверка на совпадение владельца карты и пользователя, запросившего баланс, если совпадает, формируется результат
     *
     * @param idCard на вход передается идентификатор карты в формате String
     * @return HashMap<Long, String> возвращает пару значений: баланс и номер карты
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    public HashMap<Long, String> getBalance(String idCard) {
        Person person = getPersonFromAuthority();
        Card card = getByID(UUID.fromString(idCard));
        HashMap<Long, String> result = new HashMap<>();
        if (card.getOwner().equals(person)) {
            result.put(card.getBalance(), card.getNumberOfCard());
            return result;
        } else return null;
    }

    /**
     * Метод сохранения банковской карты {@link Card} в базе данных
     * Метод переводит {@link CardDTO} в объект {@link Card}, сохраняет в базе данных
     *
     * @param cardDTO на вход передается объект банковской карты в форме DTO
     * @return CardDTO возвращает измененный объект
     * @throws NotFoundException - возникает если не найден клиент по переданному в DTO идентификатору
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public CardDTO saveCard(CardDTO cardDTO) {
        Card card = mapToCard(cardDTO);
        return map(cardRepository.save(card));

    }

    /**
     * Метод удаления банковской карты из базы данных
     *
     * @param id на вход передается уникальный идентификатор карты
     * @throws IllegalArgumentException - если передан не верный формат идентификатора карты
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void deleteById(String id) {
        cardRepository.deleteById(UUID.fromString(id));
    }

    /**
     * Метод изменения банковской карты
     * Метод сначала в базе данных находит существующий объект, затем изменяет его согласно запросу
     *
     * @param card на вход передается объект {@link CardDTO} с измененными полями
     * @return {@link CardDTO}  возвращает измененный объект
     * @throws NotFoundException - возникает если карта с идентификатором из {@link CardDTO} не найдена
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CardDTO updateCard(CardDTO card) {
        getByID(UUID.fromString(card.id()));
        return saveCard(card);
    }

    /**
     * Метод блокировки банковской карты
     * метод находит в базе данных карту, проверяет что пользователь с ролью ROLE_USER является владельцем карты
     * Устанавливает статус карты {@class StateOfCard.BLOCK}, затем записывает изменения в базу данных
     *
     * @param idCard на вход поступает идентификатор карты
     * @return {@link CardDTO} - возвращает измененный объект с новым статусом
     * @throws NotFoundException - возникает если карта с указанным идентификатором не найдена
     */
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @Transactional
    public CardDTO blockedCard(String idCard) {
        Card card = getByID(UUID.fromString(idCard));
        if (getRoleFromAuthority().equals("ROLE_USER")) {
            if (getPersonFromAuthority().getId().equals(card.getOwner().getId())) {
                card.setState(StateOfCard.BLOCK);
            } else return null;
        } else {
            card.setState(StateOfCard.BLOCK);
        }
        return map(cardRepository.save(card));
    }

    /**
     * Метод получения банковской карты из базы данных по идентификатору
     *
     * @param idCard на вход поступает идентификатор карты
     * @return {@link Card} возвращает банковскую карту
     * @throws NotFoundException - возникает если карта с указанным идентификатором не найдена
     */
    public Card getByID(UUID idCard) {
        return cardRepository.findById(idCard).orElseThrow(() -> new NotFoundException("Карта с id =  " + idCard + " не найдена"));
    }

    /**
     * Метод получения постраничного списка банковских кард, отфильтрованных по владельцу
     * метод выполняет поиск выладельца карты {@link Person} по строковому идентификатору,
     * затем получает из базы данных постраничный список карт
     *
     * @param id   на вход поступает идентификатор клиента
     * @param page параметры пагинации page=0&size=3 - номер страницы и количество элементов на странице
     * @return Page<Card> возвращает постраничный список банковских карт
     * @throws NotFoundException - возникает если клиен с таким идентификатором не найден
     */
    private Page<Card> getByOwnerId(String id, Pageable page) {
        Person person = getCardByPersonId(id);
        return cardRepository.findByOwner(person, page);
    }

    /**
     * Метод получения клиента (владельца карты) по идентификатору
     *
     * @param id на вход поступает идентификатор клиента
     * @return {@link Person} возвращается объект сущности клиент
     * @throws NotFoundException - возникает если клиен с таким идентификатором не найден
     */
    private Person getCardByPersonId(String id) {
        return personRepository.findById(UUID.fromString(id)).orElseThrow(() -> new NotFoundException("Person с id " + id + " не найден"));
    }

    /**
     * Метод возвращает фильтрованный по пользователю и статусу постраничный список банковских карт
     *
     * @param id    - идентификатор клиента
     * @param state - статус карты
     * @param page  - параметры пагинации page=0&size=3 - номер страницы и количество элементов на странице
     * @return Page<Card> возвращает постраничный список банковских карт
     * @throws NotFoundException - возникает если клиен с таким идентификатором не найден
     */
    private Page<Card> getByOwnerIdAndState(String id, StateOfCard state, Pageable page) {
        Person person = getCardByPersonId(id);
        return cardRepository.findByOwnerAndState(person, state, page);
    }

    /**
     * Метод получения доступных банковских карт
     * Метод запрашивает в базе данных банковскую карту по идентификатору,
     * Затем проверяет что статус карты {@class StateOfCard.ACTIVE}
     *
     * @param idCard - идентификатор карты
     * @return {@link Card} возвращает банковскую карту
     * @throws NotFoundException           - возникает если карта с указанным идентификатором не найдена
     * @throws IncorrectInputDataException - возникает если статус карты отличется от {@class StateOfCard.ACTIVE}
     */
    public Card getCardIfAvailable(UUID idCard) {
        Card card = cardRepository.findById(idCard).orElseThrow(() -> new NotFoundException("Карта с id =  " + idCard + " не найдена"));
        if (!(card.getState() == StateOfCard.ACTIVE)) {
            throw new IncorrectInputDataException("Карта" + card.getState().getTitle());
        }
        return card;
    }

    /**
     * Метод изменяет баланс карты
     * Метод присваивает объекту {@link Card} новый баланс
     * Сохраняет изменения в базе данных
     *
     * @param card   - объект карты {@link Card}, для которой необходимо изменить баланс
     * @param amount - величина, на которую необходимо изменить баланс
     */
    public void changeAmount(Card card, Long amount) {
        card.setBalance(card.getBalance() + amount);
        cardRepository.save(card);

    }

    /**
     * Метод преобразования объекта {@link Card} в {@link CardDTO}
     * Метод выполняет дешифрование номера карты и преобразование его в формат вывода клиенту
     * далее выполняет преобразование измененного объекта {@link Card} в {@link CardDTO}
     *
     * @param card на вход поступает объект {@link Card}
     * @return {@link CardDTO}
     */
    private CardDTO map(Card card) {
        String decryptingNumber = cryptoService.decrypt(card.getNumberOfCard());
        return mapper.toCardDTO(card, card.getOwner().getId().toString(), "**** **** **** " + decryptingNumber.substring(decryptingNumber.length() - 4));
    }

    /**
     * Метод преобразования объекта {@link CardDTO} в {@link Card}
     * Метод по идентификатору получает клиента {@link Person} из репозитория,
     * выполняет шифрование номера банковской карты
     * на основании полученных данных преобразует {@link CardDTO} в {@link Card}
     *
     * @param card на вход поступает объект {@link CardDTO}
     * @return {@link Card} возвращает полученный объект банковской карты
     */
    private Card mapToCard(CardDTO card) {
        Person person = getCardByPersonId(card.personId());
        String numberOfCardEncrypted = cryptoService.encrypt(card.numberOfCard());
        return mapper.toCard(card, person, numberOfCardEncrypted);
    }

    /**
     * Метод для получения роли пользователя {@link ru.project.CardManagementService.entity.Role} из авторизационного токена
     *
     * @return String - значение роли пользователя
     */
    private String getRoleFromAuthority() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream().findFirst().get().getAuthority();
    }

    /**
     * Метод для получения {@link Person} из авторизационного токена
     * Метод получает идентификатор клиента из авторизационного токена
     * Затем по идентификатору получает клиента из базы данных
     *
     * @return {@link Person} - клиент
     * @throws NotFoundException - возникает если клиен с таким идентификатором не найден
     */
    private Person getPersonFromAuthority() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = ((UserDto) auth.getPrincipal()).getId();
        return personRepository.findByUserId(UUID.fromString(userId)).orElseThrow(() -> new NotFoundException("Person с userId = " + userId + " не найден"));
    }

}
