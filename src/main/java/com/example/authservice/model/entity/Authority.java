package com.example.authservice.model.entity;

import com.example.authservice.model.enums.AuthorityType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "AUTHORITY")
@Data
public class Authority {

    @Id
    @Column(name = "AUTHORITY_NAME")
    @Enumerated(EnumType.STRING)
    private AuthorityType name;
}
