package com.example.meetontest.api.entities;


import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Users")
@Getter @Setter @NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    private String username;
    private String about;
    private String password;
    private String email;
    private String firstName;
    private String secondName;
    private double karma;
    private String status;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User(String username, String password, String email, String about, String firstName, String secondName, double karma, String status, Set<Role> roles) {
        this.username = username;
        this.about = about;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.secondName = secondName;
        this.karma = karma;
        this.status = status;
        this.roles = roles;
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.about = about;
        this.password = password;
        this.email = email;
    }
}
