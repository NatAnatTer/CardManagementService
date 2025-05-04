package ru.project.CardManagementService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.project.CardManagementService.dto.CardDTO;
import ru.project.CardManagementService.entity.Card;
import ru.project.CardManagementService.entity.StateOfCard;
import ru.project.CardManagementService.mapper.CardMapper;
import ru.project.CardManagementService.repository.CardRepository;

import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class CardService {
    private final CardRepository cardRepository;
    private final CardMapper mapper;

    public List<CardDTO> getCards() {
        return mapper.toCardDTOList(cardRepository.findAll());
    }

    public Page<CardDTO> getCardsP(Pageable pageable) {
      Page<Card> card =  cardRepository.findAll((pageable));
        List<CardDTO> listCardDto = card.getContent().stream().map(mapper::toCardDTO).toList();
        return new PageImpl<CardDTO>(listCardDto, pageable, listCardDto.size());
    }

    public CardDTO saveCard(CardDTO card) {
        Card newCard = cardRepository.save(mapper.toCard(card));
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
