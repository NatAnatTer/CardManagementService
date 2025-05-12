package ru.project.CardManagementService.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.project.CardManagementService.dto.OperationOfTransactionDTO;
import ru.project.CardManagementService.entity.OperationOfTransaction;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OperationOfTransactionMapper {
    OperationOfTransactionDTO toOperationOfTransactionDTO(OperationOfTransaction operationOfTransaction);

    OperationOfTransaction toOperationOfTransaction(OperationOfTransactionDTO operationOfTransactionDTO);
}
