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

    CardDTO toCardDTO(Card card);

    List<CardDTO> toCardDTOList(List<Card> card);

    @Mapping(target = "id", source = "cardDTO.id")
    @Mapping(target = "owner", source = "owner")
    Card toCard(CardDTO cardDTO, Person owner);
}
