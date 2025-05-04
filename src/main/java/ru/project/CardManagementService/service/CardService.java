package ru.project.CardManagementService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.project.CardManagementService.dto.CardDTO;
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

    public List<CardDTO> getCards() {
        List<Card> listCards = cardRepository.findAll();
        List<CardDTO> listCardDto = listCards.stream().map(this::map).toList();
        return listCardDto;
    }


    public Page<CardDTO> getCardsByFilter(Pageable pageable, Map<String, Object> query) {
        List<CardDTO> listCardDto = new ArrayList<>();
        if (!query.isEmpty()) {

            Page<Card> result = null;
            for (String field : query.keySet()) {
                String value = String.valueOf(query.get(field));
                switch (field) {

                    case ("id"):
                        Person owner = personRepository.findById(UUID.fromString(value)).orElseThrow(() -> new IllegalArgumentException("No person with id:" + value));
                        result = cardRepository.findByOwner(owner, pageable);
                        break;
                    case ("state"):
                        StateOfCard state = StateOfCard.valueOf(value);
                        result = cardRepository.findByState(state, pageable);
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
        Page<Card> card = cardRepository.findAll(pageable);
        listCardDto = card.getContent().stream().map(this::map).toList();
        return new PageImpl<>(listCardDto, pageable, listCardDto.size());
    }


    public CardDTO saveCard(CardDTO card) {
        Card cardNew = cardRepository.save(mapToCard(card));
        return map(cardNew);

    }

    public void deleteById(String id) {
        cardRepository.deleteById(UUID.fromString(id));
    }

    public CardDTO updateCard(CardDTO card) {
        cardRepository.findById(UUID.fromString(card.id())).orElseThrow(() -> new IllegalArgumentException("Card not exist with id: " + card.id()));
        return saveCard(card);
    }

    public CardDTO blockedCard(CardDTO cardDTO){
        Card card = mapToCard(cardDTO);
        card.setState(StateOfCard.BLOCK);
       return map(cardRepository.save(card));
    }

    public Card getByID(UUID idCard) {
        return cardRepository.findById(idCard).orElseThrow(() -> new IllegalArgumentException("Card not found with id:" + idCard));
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

    public CardDTO map(Card card) {
        String decryptingNumber = cryptoService.decrypt(card.getNumberOfCard());
        return mapper.toCardDTO(card, "**** **** **** " + decryptingNumber.substring(decryptingNumber.length() - 4));
    }

    public Card mapToCard(CardDTO card) {
        Person person = personRepository.findById(UUID.fromString(card.owner().id())).orElseThrow(() -> new IllegalArgumentException("not found person with id:" + card.owner().id()));
        String numberOfCardEncrypted = cryptoService.encrypt(card.numberOfCard());
        return mapper.toCard(card, person, numberOfCardEncrypted);
    }


}
