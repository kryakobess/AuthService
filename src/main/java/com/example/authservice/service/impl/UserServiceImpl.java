package com.example.authservice.service.impl;

import com.example.authservice.model.dto.AddUserRequest;
import com.example.authservice.model.entity.Role;
import com.example.authservice.model.entity.User;
import com.example.authservice.model.exception.NotFoundException;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.service.RoleService;
import com.example.authservice.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new NotFoundException(String.format("User with username:%s not found", username))
                );
    }

    @Override
    public User createUser(String username, String password, String email) {
        boolean alreadyExists = userRepository.existsByUsername(username);
        if (alreadyExists) {
            throw new IllegalStateException(String.format("User with username %s already exists", username));
        }

        var encoder = new BCryptPasswordEncoder();
        var encodedPassword = encoder.encode(password);
        return userRepository.save(new User(username, encodedPassword, email));
    }

    @Override
    @Transactional
    public User changeRolesForUser(Long id, List<String> roles) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User does not exist"));
        var allRoles = roleService.getAll();

        if (hasUnknownRoles(roles, allRoles)) {
            throw new IllegalStateException("Contains unknown roles");
        } else {
            var rolesSet = new HashSet<>(roles);
            List<Role> rolesToChange = allRoles.stream()
                    .filter(role -> rolesSet.contains(role.getRoleName()))
                    .toList();
            user.setRoles(rolesToChange);
            return userRepository.save(user);
        }
    }

    private boolean hasUnknownRoles(List<String> roles, Set<Role> allRoles) {
        var allRoleNames = allRoles.stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());
        var unknownRoles = roles.stream()
                .filter(role -> !allRoleNames.contains(role))
                .toList();
        return !unknownRoles.isEmpty();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            var user = findByUsername(username);
            return org.springframework.security.core.userdetails.User.builder()
                    .username(username)
                    .authorities(user.getAuthorities())
                    .password(user.getPassword())
                    .build();
        } catch (NotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}
