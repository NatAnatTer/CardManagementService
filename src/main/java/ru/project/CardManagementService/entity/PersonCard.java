package ru.project.CardManagementService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PersonCard {
    @Id
    @GeneratedValue
    private UUID id;

    private long numberOfCard;

    @ManyToOne
    @JoinColumn(name = "personId")
    private Person owner;

    private long expirationDate;
}
