package ru.project.CardManagementService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StateOfTransactionRepository extends JpaRepository<StateOfTransaction, UUID> {
}
