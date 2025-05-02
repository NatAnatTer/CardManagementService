package ru.project.CardManagementService.entity;

import jakarta.persistence.*;
import lombok.*;

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
  //  @JoinColumn(name = "fromCard")
    private UUID fromCard;
 //   @JoinColumn(name = "toCard")
    private UUID toCard;
    private long dateOfTransfer;
    private long amount;
    @JoinColumn(name = "state")
    private StateOfTransaction state;

}
