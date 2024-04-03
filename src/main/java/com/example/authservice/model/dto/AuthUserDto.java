package com.example.authservice.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class AuthUserDto {
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
}
