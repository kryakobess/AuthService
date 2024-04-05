package com.example.authservice.controller;

import com.example.authservice.model.enums.AuthorityType;
import com.example.authservice.model.enums.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/dictionary")
@RequiredArgsConstructor
public class DictionaryController {

    @GetMapping("/roles")
    public List<String> getRoles() {
        return Arrays.stream(RoleType.values())
                .map(RoleType::name)
                .toList();
    }

    @GetMapping("/authorities")
    public List<String> getAuthorities() {
        return Arrays.stream(AuthorityType.values())
                .map(AuthorityType::name)
                .toList();
    }
}
