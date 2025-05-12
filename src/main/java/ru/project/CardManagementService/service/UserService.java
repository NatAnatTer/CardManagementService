package ru.project.CardManagementService.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.CardManagementService.dto.UserDto;
import ru.project.CardManagementService.entity.Person;
import ru.project.CardManagementService.entity.Role;
import ru.project.CardManagementService.entity.User;
import ru.project.CardManagementService.entity.UserRole;
import ru.project.CardManagementService.mapper.UserMapper;
import ru.project.CardManagementService.repository.PersonRepository;
import ru.project.CardManagementService.repository.UserRepository;
import ru.project.CardManagementService.repository.UserRoleRepository;

import java.util.Collections;
import java.util.Optional;

/**
 * сервис для реализации логики работы с учетной записью пользователями {@link User}
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository repository;
    private final UserRoleRepository roleRepository;
    private final PersonRepository personRepository;
    private final UserMapper mapper;

    /**
     * Метод сохранения пользователя в базе данных
     * Метод сначала преобразует объект {@link UserDto} в {@link User}
     * Затем сохраняет в репозиторий
     * Затем создает для этого пользователя роль {@class Role.ROLE_USER} в {@link UserRole}
     * Далее создается запись в таблице клиентов
     *
     * @param user объект, содержащий информацию о создаваемом пользователе в формате {@link UserDto}
     * @return объект {@link UserDto}, который вернулся в результате сохранения пользователя в базе данных
     */
    @Transactional
    public UserDto save(UserDto user) {
        User userEntity = mapper.toEntity(user);
        User res = repository.save(userEntity);
        UserRole roleEntity = new UserRole();
        roleEntity.setUser(res);
        roleEntity.setRole(Role.ROLE_USER.getRoleName());
        res.setRoles(Collections.singleton(roleRepository.save(roleEntity)));
        createPerson(res);
        return mapper.toDto(res);
    }

    /**
     * Метод получения пользователя по логину
     *
     * @param login логин пользователя
     * @return Optional {@link UserDto} объект пользователя
     */
    public Optional<UserDto> getUserByName(String login) {
        return repository.findByLogin(login).map(mapper::toDto);
    }

    /**
     * Метод получения  информации о пользователе по логину
     *
     * @param login логин пользвоателя
     * @return {@link UserDetails} данные учетной записи
     * @throws UsernameNotFoundException - возникает если пользователь не найден
     */
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return getUserByName(login).orElseThrow(() -> new InternalAuthenticationServiceException("Пользователь не найден " + login));
    }

    /**
     * Создание клиента
     * Выполняется создание объекта {@link Person} и сохранение в базу данных
     *
     * @param user объект с информацией о пользвоателе {@link User}
     */
    @Transactional
    private void createPerson(User user) {
        Person person = new Person();
        person.setUserId(user.getId());
        person.setName(user.getName());
        personRepository.save(person);
    }
}
