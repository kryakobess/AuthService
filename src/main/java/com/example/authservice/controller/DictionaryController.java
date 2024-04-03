package com.example.authservice.controller;

import com.example.authservice.model.entity.Role;
import com.example.authservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dictionary")
@RequiredArgsConstructor
public class DictionaryController {

    private final RoleService roleService;

    @GetMapping("/roles")
    public List<String> getRoles() {
        return roleService.getAll().stream()
                .map(Role::getRoleName)
                .toList();
    }
}
