package com.example.authservice.model.entity;

import com.example.authservice.model.enums.AuthorityType;
import com.example.authservice.model.enums.RoleType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "AUTH_USER")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(allocationSize = 1, name = "user_seq", sequenceName = "user_seq")
    private Long id;
    private String username;
    @ToString.Exclude
    private String password;
    private String email;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "USER_ROLE",
            joinColumns = {@JoinColumn(name = "USER_ID")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID")}
    )
    private List<Role> roles = new ArrayList<>();

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public Set<GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> grantedRoles = roles.stream()
                .map(Role::getRoleName)
                .map(RoleType::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        Set<GrantedAuthority> grantedAuthorities = roles.stream()
                .flatMap(role -> role.getAuthorities().stream())
                .map(Authority::getName)
                .map(AuthorityType::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        grantedAuthorities.addAll(grantedRoles);
        return grantedAuthorities;
    }
}
