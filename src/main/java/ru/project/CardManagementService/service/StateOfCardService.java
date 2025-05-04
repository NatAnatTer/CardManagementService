package ru.project.CardManagementService.service;


import org.springframework.stereotype.Service;
import ru.project.CardManagementService.dto.StateOfCardDTO;
import ru.project.CardManagementService.entity.StateOfCardss;
import ru.project.CardManagementService.mapper.StateOfCardMapper;
import ru.project.CardManagementService.repository.StateOfCardRepository;

import java.util.List;
import java.util.UUID;


@Service
public class StateOfCardService {
    private final StateOfCardRepository repository;
    private final StateOfCardMapper mapper;

    public StateOfCardService(StateOfCardRepository stateOfCardRepository, StateOfCardMapper mapper) {
        this.repository = stateOfCardRepository;
        this.mapper = mapper;
    }

    public List<StateOfCardDTO> getAll() {
        return mapper.toStateOfCardDTOList(repository.findAll());
    }

    public StateOfCardDTO createState(StateOfCardDTO state) {
        StateOfCardss stateOfCard = repository.save(mapper.toStateOfCard(state));
        return mapper.toStateOfCardDTO(stateOfCard);
    }

    public void deleteState(String id) {
        repository.deleteById(UUID.fromString(id));
    }

    public StateOfCardDTO updateState(String id, StateOfCardDTO state) {
        repository.findById(UUID.fromString(id)).orElseThrow(() -> new IllegalArgumentException("State not found with id:" + id));
        return createState(state);
    }

}
