package com.example.authservice.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChangeRolesRequest {
    List<String> roles;
}
