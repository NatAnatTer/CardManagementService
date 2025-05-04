package ru.project.CardManagementService.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.project.CardManagementService.dto.StateOfCardDTO;
import ru.project.CardManagementService.entity.StateOfCardss;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StateOfCardMapper {
    StateOfCardDTO toStateOfCardDTO(StateOfCardss state);

    List<StateOfCardDTO> toStateOfCardDTOList(List<StateOfCardss> stateOfCardList);

    StateOfCardss toStateOfCard(StateOfCardDTO stateOfCardDTO);

    List<StateOfCardss> toStateOfCardList(List<StateOfCardDTO> stateOfCardDTOList);


}
