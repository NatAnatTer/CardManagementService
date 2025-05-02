package ru.project.CardManagementService.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.project.CardManagementService.dto.StateOfCardDTO;
import ru.project.CardManagementService.entity.StateOfCard;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StateOfCardMapper {
    StateOfCardDTO toStateOfCardDTO(StateOfCard state);

    List<StateOfCardDTO> toStateOfCardDTOList(List<StateOfCard> stateOfCardList);

    StateOfCard toStateOfCard(StateOfCardDTO stateOfCardDTO);

    List<StateOfCard> toStateOfCardList(List<StateOfCardDTO> stateOfCardDTOList);


}
