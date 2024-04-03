package com.example.authservice.service.validation;

import com.example.authservice.model.dto.AddUserRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserValidator {
    public void validateCreation(AddUserRequest request) {
        if (Strings.isBlank(request.getUsername())) {
            throw new IllegalStateException("Username must not be empty");
        }
        if (Strings.isBlank(request.getPassword())) {
            throw new IllegalStateException("Password must not be empty");
        }
    }
}
