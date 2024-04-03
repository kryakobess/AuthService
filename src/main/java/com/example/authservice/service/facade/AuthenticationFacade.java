package com.example.authservice.service.facade;

import com.example.authservice.model.dto.OuterUserDto;
import com.example.authservice.model.dto.TokenDto;

public interface AuthenticationFacade {
    TokenDto login(OuterUserDto userDto);
}
