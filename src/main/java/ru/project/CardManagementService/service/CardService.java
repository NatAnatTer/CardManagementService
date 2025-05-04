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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class CardService {
    private final CardRepository cardRepository;
    private final PersonRepository personRepository;
    private final CardMapper mapper;

    public List<CardDTO> getCards() {
        return mapper.toCardDTOList(cardRepository.findAll());
    }

//    public Page<CardDTO> getCardsPageable(Pageable pageable) {
//        Page<Card> card = cardRepository.findAll((pageable));
//        List<CardDTO> listCardDto = card.getContent().stream().map(mapper::toCardDTO).toList();
//        return new PageImpl<>(listCardDto, pageable, listCardDto.size());
//    }

    public Page<CardDTO> getCardsByFilter(Pageable pageable, Map<String, Object> query) {
        Page<Card> card = cardRepository.findAll(pageable);
        List<CardDTO> listCardDto = card.getContent().stream().map(mapper::toCardDTO).toList();
        if (!query.isEmpty()) {

            Page<Card> result = null;
            for (String field : query.keySet()) {
                String value = String.valueOf(query.get(field));
            switch (field) {

                    case ("id"):
                        Person owner = personRepository.findById(UUID.fromString(value)).orElseThrow(()->new IllegalArgumentException("No person with id:"+ value));
                        result = cardRepository.findByOwner(owner, pageable);
                        break;
                    case ("numberOfCard"):

                       result = cardRepository.findLikeByNumberOfCard(value, pageable);
                       break;
                    case ("state"):
                        StateOfCard state = StateOfCard.valueOf(value);
                        result = cardRepository.findByState(state, pageable);
                        break;
                    default:
                        continue;
                }
            }
            if(Objects.nonNull(result)){
                List<CardDTO> filteredCardDTO = result.getContent().stream().map(mapper::toCardDTO).toList();
                return new PageImpl<>(filteredCardDTO, pageable, listCardDto.size());
            }
        }
        return new PageImpl<>(listCardDto, pageable, listCardDto.size());

    }


    public CardDTO saveCard(CardDTO card) {
        Person person = personRepository.findById(UUID.fromString(card.owner().id())).orElseThrow(() -> new IllegalArgumentException("not found person with id:" + card.owner().id()));
        Card newCard = cardRepository.save(mapper.toCard(card, person));
        return mapper.toCardDTO(newCard);
    }

    public void deleteById(String id) {
        cardRepository.deleteById(UUID.fromString(id));
    }

    public CardDTO updateCard(String id, CardDTO card) {
        cardRepository.findById(UUID.fromString(id)).orElseThrow(() -> new IllegalArgumentException("Card not exist with id: " + id));

        return saveCard(card);
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


}
