package ru.project.CardManagementService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.project.CardManagementService.entity.StateOfTransaction;

import java.util.UUID;

public interface StateOfTransactionRepository extends JpaRepository<StateOfTransaction, UUID> {
}
