package ru.project.CardManagementService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.project.CardManagementService.dto.CardDTO;
import ru.project.CardManagementService.dto.UserDto;
import ru.project.CardManagementService.entity.Card;
import ru.project.CardManagementService.entity.Person;
import ru.project.CardManagementService.entity.StateOfCard;
import ru.project.CardManagementService.mapper.CardMapper;
import ru.project.CardManagementService.repository.CardRepository;
import ru.project.CardManagementService.repository.PersonRepository;
import ru.project.CardManagementService.security.CryptoService;

import java.util.*;


@RequiredArgsConstructor
@Service
public class CardService {
    private final CardRepository cardRepository;
    private final PersonRepository personRepository;
    private final CardMapper mapper;
    private final CryptoService cryptoService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<CardDTO> getCards() {
        List<Card> listCards = cardRepository.findAll();
        return listCards.stream().map(this::map).toList();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public Page<CardDTO> getCardsByFilter(Pageable pageable, Map<String, Object> query) {
        List<CardDTO> listCardDto;
        if (!query.isEmpty()) {

            Page<Card> result = null;
            for (String field : query.keySet()) {
                String value = String.valueOf(query.get(field));
                switch (field) {
                    case ("id"):
                        result = getByOwnerId(value, pageable);
//                        Person owner = personRepository.findById(UUID.fromString(value)).orElseThrow(() -> new IllegalArgumentException("No person with id:" + value));
//                        result = cardRepository.findByOwner(owner, pageable);
                        break;
                    case ("state"):
                        if (getRoleFromAuthority().equals("ROLE_ADMIN")) {
                            StateOfCard state = StateOfCard.valueOf(value);
                            result = cardRepository.findByState(state, pageable);
                        } else {
                            result = getByOwnerIdAndState(getPersonFromAuthority().getId().toString(), StateOfCard.valueOf(value), pageable);
//                            StateOfCard state = StateOfCard.valueOf(value);
//                            result = cardRepository.findByState(state, pageable);
                        }
                        break;
                    default:
                        continue;
                }
            }
            if (Objects.nonNull(result)) {
                List<CardDTO> filteredCardDTO = result.getContent().stream().map(this::map).toList();
                return new PageImpl<>(filteredCardDTO, pageable, filteredCardDTO.size());
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
//        Page<Card> card = cardRepository.findAll(pageable);
//        listCardDto = card.getContent().stream().map(this::map).toList();
//        return new PageImpl<>(listCardDto, pageable, listCardDto.size());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public HashMap<Long,String> getBalance(String idCard) {
        Person person = getPersonFromAuthority();
        Card card = cardRepository.findById(UUID.fromString(idCard)) .orElseThrow(() -> new IllegalArgumentException("Card not exist with id: " + idCard));
        HashMap<Long, String> result = new HashMap<>();
        if(card.getOwner().equals(person)){
           result.put(card.getBalance(), card.getNumberOfCard());
           return result;
        } else return null;
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CardDTO saveCard(CardDTO cardDTO) {
        Person person = personRepository.findById(UUID.fromString(cardDTO.personId())).orElseThrow(() -> new IllegalArgumentException("Person not found" + cardDTO.personId()));
        Card card = mapToCard(cardDTO, person);
        card.setOwner(person);
        return map(cardRepository.save(card));

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteById(String id) {
        cardRepository.deleteById(UUID.fromString(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CardDTO updateCard(CardDTO card) {
        cardRepository.findById(UUID.fromString(card.id())).orElseThrow(() -> new IllegalArgumentException("Card not exist with id: " + card.id()));
        return saveCard(card);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public CardDTO blockedCard(CardDTO cardDTO) {
        Person person = personRepository.findById(UUID.fromString(cardDTO.personId())).orElseThrow(() -> new IllegalArgumentException("Person not exist with id: " + cardDTO.id()));
        Card card = mapToCard(cardDTO, person);
        card.setState(StateOfCard.BLOCK);
        return map(cardRepository.save(card));
    }

    public Card getByID(UUID idCard) {
        return cardRepository.findById(idCard).orElseThrow(() -> new IllegalArgumentException("Card not found with id:" + idCard));
    }

    private Page<Card> getByOwnerId(String id, Pageable page) {
        Person person = personRepository.findById(UUID.fromString(id)).orElseThrow(() -> new IllegalArgumentException("not found person" + id));
        return cardRepository.findByOwner(person, page);
    }

    private Page<Card> getByOwnerIdAndState(String id, StateOfCard state, Pageable page) {
        Person person = personRepository.findById(UUID.fromString(id)).orElseThrow(() -> new IllegalArgumentException("not found person" + id));
        return cardRepository.findByOwnerAndState(person, state, page);
    }

    public Card getCardIfAvailable(UUID idCard) {
        Card card = cardRepository.findById(idCard).orElseThrow(() -> new IllegalArgumentException("Card not found with id:" + idCard));
        if (card.getState() == StateOfCard.BLOCK || card.getState() == StateOfCard.EXPIRED) {
            throw new IllegalArgumentException("Карта" + card.getState().getTitle());
        }
        return card;
    }

    public void changeAmount(Card card, Long amount) {
        card.setBalance(card.getBalance() + amount);
        cardRepository.save(card);

    }

    private CardDTO map(Card card) {
        String decryptingNumber = cryptoService.decrypt(card.getNumberOfCard());
        return mapper.toCardDTO(card, card.getOwner().toString(), "**** **** **** " + decryptingNumber.substring(decryptingNumber.length() - 4));
    }

    private Card mapToCard(CardDTO card, Person person) {
        String numberOfCardEncrypted = cryptoService.encrypt(card.numberOfCard());
        return mapper.toCard(card, person, numberOfCardEncrypted);
    }

    private String getRoleFromAuthority() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream().findFirst().get().getAuthority();
    }

    private Person getPersonFromAuthority() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = ((UserDto) auth.getPrincipal()).getId();
        return personRepository.findByUserId(UUID.fromString(userId)).orElseThrow(() -> new IllegalArgumentException("Person not found with userId = " + userId));
    }

}
