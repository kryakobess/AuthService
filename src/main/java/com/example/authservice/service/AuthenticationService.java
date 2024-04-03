package com.example.authservice.service;

import com.example.authservice.model.dto.OuterUserDto;

public interface AuthenticationService {
    String authenticateAngGenerateToken(OuterUserDto user);
}
