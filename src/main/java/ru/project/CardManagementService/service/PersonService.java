package ru.project.CardManagementService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.CardManagementService.dto.PersonDTO;
import ru.project.CardManagementService.entity.Person;
import ru.project.CardManagementService.exception.NotFoundException;
import ru.project.CardManagementService.mapper.PersonMapper;
import ru.project.CardManagementService.repository.PersonRepository;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для реализации логики работы с сущностью клиента {@link Person}
 */
@RequiredArgsConstructor
@Service
public class PersonService {
    private final PersonRepository repository;
    private final PersonMapper mapper;

    /**
     * Метод получения списка клиентов, доступен пользователю с ролью {@class Role.ROLE_ADMIN}
     *
     * @return список всех клиентов
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<PersonDTO> getAll() {
        return mapper.toPersonDTOList(repository.findAll());
    }

    /**
     * Метод создания нового клиента
     *
     * @param personDTO объект {@link PersonDTO}, содержащий информацию о новом клиенте
     * @return только что созданный объект {@link PersonDTO}
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public PersonDTO createPerson(PersonDTO personDTO) {
        Person person = repository.save(mapper.toPerson(personDTO));
        return mapper.toPersonDTO(person);
    }

    /**
     * Метод удаления существующего клиента
     *
     * @param id идентификатор клиента
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void deletePerson(String id) {
        repository.deleteById(UUID.fromString(id));
    }

    /**
     * Метод изменения существующего клиента
     *
     * @param personDTO объект {@link PersonDTO}, содержащий информацию о клиенте с новыми значениями полей
     * @return только что измененный объект {@link PersonDTO}
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public PersonDTO updatePerson(PersonDTO personDTO) {
        repository.findById(UUID.fromString(personDTO.id())).orElseThrow(() -> new NotFoundException("Клиент с идентификатором id: " + personDTO.id() + " не найден"));
        return createPerson(personDTO);
    }

}
