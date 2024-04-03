package com.example.authservice.service.facade.impl;

import com.example.authservice.model.dto.OuterUserDto;
import com.example.authservice.model.dto.TokenDto;
import com.example.authservice.service.AuthenticationService;
import com.example.authservice.service.facade.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    private final AuthenticationService authenticationService;

    @Override
    public TokenDto login(OuterUserDto userDto) {
        log.info("Logging attempt by {}", userDto.getUsername());
        var jwt = authenticationService.authenticateAngGenerateToken(userDto);
        return new TokenDto(jwt);
    }
}
