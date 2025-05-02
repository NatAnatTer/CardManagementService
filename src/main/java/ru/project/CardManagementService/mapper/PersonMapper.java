package ru.project.CardManagementService.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.project.CardManagementService.dto.PersonDTO;
import ru.project.CardManagementService.entity.Person;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PersonMapper {
    PersonDTO toPersonDTO(Person person);

    List<PersonDTO> toPersonDTOList(List<Person> personList);

    Person toPerson(PersonDTO personDTO);


}


