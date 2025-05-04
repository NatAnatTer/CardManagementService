package ru.project.CardManagementService.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class StateOfCardss {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;

//    public StateOfCard(StateOfCardDTO state){
//        super();
//        this.name = state.getName();
//    }
}
