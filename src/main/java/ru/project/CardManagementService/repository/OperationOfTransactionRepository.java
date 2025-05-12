package ru.project.CardManagementService.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.CardManagementService.entity.OperationOfTransaction;

import java.util.UUID;

public interface OperationOfTransactionRepository extends JpaRepository<OperationOfTransaction, UUID> {
    Page<OperationOfTransaction> findByFromCard(UUID fromCard, Pageable pageable);
}
