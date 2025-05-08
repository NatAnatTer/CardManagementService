package ru.project.CardManagementService.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository repository;
    private final UserRoleRepository roleRepository;
    private final PersonRepository personRepository;
    private final UserMapper mapper;

    public UserDto save(UserDto user){
        User userEntity = mapper.toEntity(user);
        User res = repository.save(userEntity);
        UserRole roleEntity = new UserRole();
        roleEntity.setUser(res);
        roleEntity.setRole(Role.ROLE_USER.getRoleName());
        res.setRoles(Collections.singleton(roleRepository.save(roleEntity)));
        createPerson(res);
        return mapper.toDto(res);
    }

    public Optional<UserDto> getUserByName(String login){
        return repository.findByLogin(login).map(mapper::toDto);
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return getUserByName(login).orElseThrow(() -> new InternalAuthenticationServiceException("Пользователь не найден " + login));
    }

    private void createPerson(User user){
        Person person = new Person();
        person.setUserId(user.getId());
        person.setName(user.getName());
        personRepository.save(person);
    }
}
