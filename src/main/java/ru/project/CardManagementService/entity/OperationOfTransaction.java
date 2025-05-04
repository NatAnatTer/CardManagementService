package ru.project.CardManagementService.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OperationOfTransaction {
    @Id
    @GeneratedValue
    private UUID id;
    private UUID fromCard;
    private UUID toCard;
    @CreatedDate
    private long dateOfTransfer;
    private long amount;
    @Enumerated(EnumType.STRING)
    private StateOfTransaction state;

}
