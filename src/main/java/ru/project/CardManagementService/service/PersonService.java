package ru.project.CardManagementService.service;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.project.CardManagementService.dto.PersonDTO;
import ru.project.CardManagementService.entity.Person;
import ru.project.CardManagementService.mapper.PersonMapper;
import ru.project.CardManagementService.repository.PersonRepository;

import java.util.List;
import java.util.UUID;

@Service
public class PersonService {
    private final PersonRepository repository;
    private final PersonMapper mapper;

    public PersonService(PersonRepository personRepository, PersonMapper personMapper) {
        this.repository = personRepository;
        this.mapper = personMapper;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<PersonDTO> getAll() {
        return mapper.toPersonDTOList(repository.findAll());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PersonDTO createPerson(PersonDTO personDTO) {
        Person person = repository.save(mapper.toPerson(personDTO));
        return mapper.toPersonDTO(person);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deletePerson(String id) {
        repository.deleteById(UUID.fromString(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PersonDTO updatePerson(PersonDTO personDTO) {
        repository.findById(UUID.fromString(personDTO.id())).orElseThrow(() -> new IllegalArgumentException("Person not found with id:" + personDTO.id()));
        return createPerson(personDTO);
    }

}
