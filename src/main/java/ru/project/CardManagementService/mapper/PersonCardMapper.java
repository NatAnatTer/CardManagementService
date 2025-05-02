package ru.project.CardManagementService.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.project.CardManagementService.dto.PersonCardDTO;
import ru.project.CardManagementService.entity.Person;
import ru.project.CardManagementService.entity.PersonCard;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PersonCardMapper {
    PersonCardDTO toPersonCardDTO(PersonCard personCard);

    List<PersonCardDTO> toPersonCardDTOList(List<PersonCard> personCards);


    @Mapping(target = "id", source = "personCardDTO.id")
    @Mapping(target = "owner", source = "person")
    PersonCard toPersonCard(PersonCardDTO personCardDTO, Person person);



}
