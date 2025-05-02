package ru.project.CardManagementService.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.project.CardManagementService.dto.CardDTO;
import ru.project.CardManagementService.entity.Card;
import ru.project.CardManagementService.entity.PersonCard;
import ru.project.CardManagementService.entity.StateOfCard;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CardMapper {
    CardDTO toCardDTO(Card card);

    List<CardDTO> toCardDTOList(List<Card> card);

    @Mapping(target = "id", source = "cardDTO.id")
    @Mapping(target = "personCard", source = "personCard")
    @Mapping(target = "state", source = "stateOfCard")
    Card toCard(CardDTO cardDTO, PersonCard personCard, StateOfCard stateOfCard);
}
