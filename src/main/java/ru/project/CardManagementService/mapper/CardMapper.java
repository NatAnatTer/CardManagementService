package ru.project.CardManagementService.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.project.CardManagementService.dto.CardDTO;
import ru.project.CardManagementService.entity.Card;
import ru.project.CardManagementService.entity.Person;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CardMapper {

    @Mapping(target = "numberOfCard", source = "newNumber")
    CardDTO toCardDTO(Card card, String newNumber);

    List<CardDTO> toCardDTOList(List<Card> card);

    @Mapping(target = "id", source = "cardDTO.id")
    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "numberOfCard", source = "newNumber")
    Card toCard(CardDTO cardDTO, Person owner, String newNumber);
}
