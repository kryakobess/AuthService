package com.example.authservice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "AUTH_ROLE")
@Data
public class Role {
    @Id
    private Integer id;
    @Column(name = "ROLE_NAME")
    private String roleName;
}
