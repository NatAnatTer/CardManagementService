package ru.project.CardManagementService.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.project.CardManagementService.dto.StateOfTransactionDTO;
import ru.project.CardManagementService.entity.StateOfTransaction;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StateOfTransactionMapper {
    StateOfTransactionDTO toStateOfTransactionDTO(StateOfTransaction stateOfTransaction);

    List<StateOfTransactionDTO> toStateOfTransactionDTOList(List<StateOfTransaction> stateOfTransaction);

    StateOfTransaction toStateOfTransaction(StateOfTransactionDTO stateOfTransactionDTO);

    List<StateOfTransaction> toStateOfTransactionList(List<StateOfTransactionDTO> stateOfTransactionDTO);
}

