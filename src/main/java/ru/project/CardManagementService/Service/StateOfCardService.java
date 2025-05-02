package ru.project.CardManagementService.Service;


import org.springframework.stereotype.Service;
import ru.project.CardManagementService.dto.StateOfCardDTO;
import ru.project.CardManagementService.entity.StateOfCard;
import ru.project.CardManagementService.mapper.StateOfCardMapper;
import ru.project.CardManagementService.repository.StateOfCardRepository;

import java.util.List;


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

    public StateOfCardDTO createState(StateOfCardDTO state){
        StateOfCard stateOfCard = repository.save(mapper.toStateOfCard(state));
       return mapper.toStateOfCardDTO(stateOfCard);
    }

}
