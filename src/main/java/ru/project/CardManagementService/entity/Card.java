package ru.project.CardManagementService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@FieldNameConstants
public class Card implements Serializable {
    @Id
    @GeneratedValue
    private UUID id;

    private String numberOfCard;

    @ManyToOne
    @JoinColumn(name = "personId")
    private Person owner;

    private long expirationDate;

    @Enumerated(EnumType.STRING)
    private StateOfCard state = StateOfCard.BLOCK;

    private long balance;
}
