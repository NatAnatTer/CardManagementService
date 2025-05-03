package ru.project.CardManagementService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.project.CardManagementService.dto.PersonCardDTO;
import ru.project.CardManagementService.entity.Person;
import ru.project.CardManagementService.entity.PersonCard;
import ru.project.CardManagementService.mapper.PersonCardMapper;
import ru.project.CardManagementService.repository.PersonCardRepository;
import ru.project.CardManagementService.repository.PersonRepository;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PersonCardService {
    private final PersonCardRepository repository;
    private final PersonRepository personRepository;
    private final PersonCardMapper mapper;

    public List<PersonCardDTO> getAll() {
        return mapper.toPersonCardDTOList(repository.findAll());
    }

    public PersonCardDTO savePersonCard(PersonCardDTO personCardDTO) {
        Person person = personRepository.findById(personCardDTO.owner().getId())
                .orElseThrow(() -> new IllegalArgumentException("Not found person with id " + personCardDTO.owner().getId()));
        PersonCard personCard = repository.save(mapper.toPersonCard(personCardDTO, person));
        return mapper.toPersonCardDTO(personCard);
    }

    public void deletePersonCard(String id) {
        repository.deleteById(UUID.fromString(id));
    }

    public PersonCardDTO updatePersonCard(String id, PersonCardDTO personCardDTO) {
        repository.findById(UUID.fromString(id)).orElseThrow(() -> new IllegalArgumentException("Person card not found with id: " + id));

        return savePersonCard(personCardDTO);
    }
}
