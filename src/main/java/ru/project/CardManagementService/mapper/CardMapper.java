package ru.project.CardManagementService.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;
import ru.project.CardManagementService.dto.CardDTO;
import ru.project.CardManagementService.entity.Card;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CardMapper {
    @Mapping(target = "cardDTO.id", source = "id")
    CardDTO toCardDTO(Card card);

    List<CardDTO> toCardDTOList(List<Card> card);

    @Mapping(target = "id", source = "cardDTO.id")
    Card toCard(CardDTO cardDTO);

}
