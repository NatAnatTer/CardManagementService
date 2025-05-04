package ru.project.CardManagementService.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.project.CardManagementService.dto.OperationOfTransactionDTO;
import ru.project.CardManagementService.entity.OperationOfTransaction;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OperationOfTransactionMapper {
    OperationOfTransactionDTO toOperationOfTransactionDTO(OperationOfTransaction operationOfTransaction);

    List<OperationOfTransactionDTO> toOperationOfTransactionDTOList(List<OperationOfTransaction> operationOfTransactions);

    OperationOfTransaction toOperationOfTransaction(OperationOfTransactionDTO operationOfTransactionDTO);
}
