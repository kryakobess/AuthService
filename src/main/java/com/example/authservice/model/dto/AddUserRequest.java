package com.example.authservice.model.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AddUserRequest {
    private String username;
    private String email;
    private String password;
}
