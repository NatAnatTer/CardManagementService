package ru.project.CardManagementService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Card implements Serializable {
    @Id
    @GeneratedValue
    private UUID id;

    private String numberOfCard;

    @ManyToOne
    @JoinColumn(name = "personId")
    private Person owner;

    private long expirationDate;

    @Enumerated(EnumType.ORDINAL)
    private StateOfCard state = StateOfCard.BLOCK;

    private long balance;
}
