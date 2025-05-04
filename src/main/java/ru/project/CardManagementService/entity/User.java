package ru.project.CardManagementService.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Set;
import java.util.UUID;


@Getter
@Setter
@Entity
@Table(name = "t_user")
public class User {

    @Id
    @GeneratedValue
    private UUID id;
    private String login;
    private String password;
    private String email;
    private String name;

    @OneToMany(mappedBy = "user")
    @Fetch(FetchMode.JOIN)
    private Set<UserRole> roles;

}
