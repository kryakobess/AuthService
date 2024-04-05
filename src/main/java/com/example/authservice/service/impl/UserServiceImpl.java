package com.example.authservice.service.impl;

import com.example.authservice.model.entity.Role;
import com.example.authservice.model.entity.User;
import com.example.authservice.model.enums.RoleType;
import com.example.authservice.model.exception.AuthenticationException;
import com.example.authservice.model.exception.NotFoundException;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.service.RoleService;
import com.example.authservice.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

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

        var encodedPassword = passwordEncoder.encode(password);
        return userRepository.save(new User(username, encodedPassword, email));
    }

    @Override
    @Transactional
    public User changeRolesForUser(Long id, List<RoleType> roles) {
        var user = findById(id);
        var allRoles = roleService.getAll();
        var rolesSet = new HashSet<>(roles);
        List<Role> rolesToChange = allRoles.stream()
                .filter(role -> rolesSet.contains(role.getRoleName()))
                .collect(Collectors.toList());
        user.setRoles(rolesToChange);
        return userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                        new NotFoundException(String.format("User with id: %d not found", id))
                );
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void remove(User userToDelete) {
        userRepository.delete(userToDelete);
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {
        User user = findByUsername(username);
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new AuthenticationException("Incorrect password");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
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
