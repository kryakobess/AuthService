package com.example.authservice.controller;

import com.example.authservice.model.dto.OuterUserDto;
import com.example.authservice.model.dto.TokenDto;
import com.example.authservice.service.facade.AuthenticationFacade;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationFacade authenticationFacade;

    @PostMapping("/login")
    @SecurityRequirements()
    public ResponseEntity<TokenDto> login(@RequestBody OuterUserDto outerUserDto) {
        return ResponseEntity.ok(authenticationFacade.login(outerUserDto));
    }
}
