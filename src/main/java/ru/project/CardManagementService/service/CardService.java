package ru.project.CardManagementService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.project.CardManagementService.dto.CardDTO;
import ru.project.CardManagementService.entity.Card;
import ru.project.CardManagementService.entity.PersonCard;
import ru.project.CardManagementService.entity.StateOfCard;
import ru.project.CardManagementService.mapper.CardMapper;
import ru.project.CardManagementService.repository.CardRepository;
import ru.project.CardManagementService.repository.PersonCardRepository;
import ru.project.CardManagementService.repository.StateOfCardRepository;

import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class CardService {
    private final CardRepository cardRepository;
    private final CardMapper mapper;
    private final PersonCardRepository personCardRepository;
    private final StateOfCardRepository stateOfCardRepository;

    public List<CardDTO> getCards() {
        return mapper.toCardDTOList(cardRepository.findAll());
    }

    public CardDTO saveCard(CardDTO card) {
        PersonCard personCard = personCardRepository.findById(card.personCard().getId())
                .orElseThrow(() -> new IllegalArgumentException("Not found person card with id " + card.personCard().getId()));
        StateOfCard state = stateOfCardRepository.findById(card.state().getId())
                .orElseThrow(() -> new IllegalArgumentException("Not found this status with id" + card.state().getId()));
        Card newCard = cardRepository.save(mapper.toCard(card, personCard, state));
        return mapper.toCardDTO(newCard);
    }

    public void deleteById(String id) {
        cardRepository.deleteById(UUID.fromString(id));
    }

    public CardDTO updateCard(String id, CardDTO card) {
        cardRepository.findById(UUID.fromString(id)).orElseThrow(() -> new IllegalArgumentException("Card not exist with id: " + id));

       return saveCard(card);
    }



}
