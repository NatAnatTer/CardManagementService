package ru.project.CardManagementService.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.project.CardManagementService.dto.PersonCardDTO;
import ru.project.CardManagementService.entity.Person;
import ru.project.CardManagementService.entity.PersonCard;
import ru.project.CardManagementService.mapper.PersonCardMapper;
import ru.project.CardManagementService.repository.PersonCardRepository;
import ru.project.CardManagementService.repository.PersonRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PersonCardService {
    private final PersonCardRepository repository;
    private final PersonRepository personRepository;
    private final PersonCardMapper mapper;

    public List<PersonCardDTO> getAll(){
        return mapper.toPersonCardDTOList(repository.findAll());
    }

    public PersonCardDTO savePersonCard(PersonCardDTO personCardDTO){
        Person person = personRepository.findById(personCardDTO.owner().getId())
                .orElseThrow(() -> new IllegalArgumentException("Not found person with id "+ personCardDTO.owner().getId()));
        PersonCard personCard = repository.save(mapper.toPersonCard(personCardDTO, person));
        return mapper.toPersonCardDTO(personCard);
    }
}
