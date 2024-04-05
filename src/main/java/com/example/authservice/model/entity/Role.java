package com.example.authservice.model.entity;

import com.example.authservice.model.enums.RoleType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(name = "AUTH_ROLE")
@Data
public class Role {
    @Id
    private Integer id;

    @Column(name = "ROLE_NAME")
    @Enumerated(EnumType.STRING)
    private RoleType roleName;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "ROLE_AUTHORITY",
            joinColumns = {@JoinColumn(name = "ROLE_ID")},
            inverseJoinColumns = {@JoinColumn(name = "AUTHORITY_NAME")}
    )
    private Set<Authority> authorities;
}
