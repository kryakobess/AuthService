package com.example.authservice.service;

import org.springframework.security.core.Authentication;

public interface JwtService {
    String generateToken(Authentication authentication);

    Authentication parseToken(String jwt);
}
