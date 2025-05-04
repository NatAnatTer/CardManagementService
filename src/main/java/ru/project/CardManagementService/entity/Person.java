package ru.project.CardManagementService.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Person {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private long serialAndNumberOfPassport;
    private long createdAt;
    private UUID userId;
}
