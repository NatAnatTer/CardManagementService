package ru.project.CardManagementService.Service;

import org.springframework.stereotype.Service;
import ru.project.CardManagementService.dto.PersonDTO;
import ru.project.CardManagementService.entity.Person;
import ru.project.CardManagementService.mapper.PersonMapper;
import ru.project.CardManagementService.repository.PersonRepository;

import java.util.List;

@Service
public class PersonService {
    private final PersonRepository repository;
    private final PersonMapper mapper;

    public PersonService(PersonRepository personRepository, PersonMapper personMapper) {
        this.repository = personRepository;
        this.mapper = personMapper;
    }

    public List<PersonDTO> getAll() {
        return mapper.toPersonDTOList(repository.findAll());
    }

    public PersonDTO createPerson(PersonDTO personDTO) {
        Person person = repository.save(mapper.toPerson(personDTO));
        return mapper.toPersonDTO(person);
    }

}
