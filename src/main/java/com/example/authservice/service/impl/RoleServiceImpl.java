package com.example.authservice.service.impl;

import com.example.authservice.model.entity.Role;
import com.example.authservice.repository.RoleRepository;
import com.example.authservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Set<Role> getAll() {
        return new HashSet<>(roleRepository.findAll());
    }
}
