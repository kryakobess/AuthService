package com.example.authservice.service.facade.impl;

import com.example.authservice.mapper.UserMapper;
import com.example.authservice.model.dto.AddUserRequest;
import com.example.authservice.model.dto.AuthUserDto;
import com.example.authservice.model.dto.ChangeRolesRequest;
import com.example.authservice.service.UserService;
import com.example.authservice.service.facade.UserFacade;
import com.example.authservice.service.validation.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserFacadeImpl implements UserFacade {

    private final UserService userService;
    private final UserValidator userValidator;
    private final UserMapper mapper;

    @Override
    public AuthUserDto addUser(AddUserRequest addUserRequest) {
        log.info("Getting POST request to add user: {}", addUserRequest.getUsername());
        userValidator.validateCreation(addUserRequest);
        return mapper.toAuthUserDto(
                userService.createUser(
                        addUserRequest.getUsername(),
                        addUserRequest.getPassword(),
                        addUserRequest.getEmail()
                )
        );
    }

    @Override
    public AuthUserDto changeRoles(Long id, ChangeRolesRequest changeRolesRequest) {
        log.info("Getting PUT changeRoles request for userId: {}, roles: {}", id, changeRolesRequest.getRoles());
        return mapper.toAuthUserDto(userService.changeRolesForUser(id, changeRolesRequest.getRoles()));
    }
}
