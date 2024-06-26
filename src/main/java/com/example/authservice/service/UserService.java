package com.example.authservice.service;

import com.example.authservice.model.entity.User;
import com.example.authservice.model.enums.RoleType;

import java.util.List;

public interface UserService {
    User findByUsername(String username);
    User createUser(String username, String password, String email);
    User changeRolesForUser(Long id, List<RoleType> roles);
    User findById(Long id);
    List<User> getAll();
    void remove(User userToDelete);
    void changePassword(String username, String oldPassword, String newPassword);
}
