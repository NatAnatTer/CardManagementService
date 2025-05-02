package ru.project.CardManagementService.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.project.CardManagementService.dto.OperationOfTransactionDTO;
import ru.project.CardManagementService.entity.Card;
import ru.project.CardManagementService.entity.OperationOfTransaction;
import ru.project.CardManagementService.entity.StateOfTransaction;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OperationOfTransactionMapper {
   // @Mapping(target = "fromCard", source = "operationOfTransaction.fromCard.id")
   // @Mapping(target = "toCard", source = "operationOfTransaction.toCard.id")
    OperationOfTransactionDTO toOperationOfTransactionDTO(OperationOfTransaction operationOfTransaction);

    List<OperationOfTransactionDTO> toOperationOfTransactionDTOList(List<OperationOfTransaction> operationOfTransactions);

    @Mapping(target = "id", source = "operationOfTransactionDTO.id")
    @Mapping(target = "fromCard", source = "fromCard")
    @Mapping(target = "toCard", source = "toCard")
    @Mapping(target = "state", source = "state")
    OperationOfTransaction toOperationOfTransaction(OperationOfTransactionDTO operationOfTransactionDTO,UUID fromCard, UUID toCard, StateOfTransaction state);


}
