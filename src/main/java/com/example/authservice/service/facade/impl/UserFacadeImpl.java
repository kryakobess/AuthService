package com.example.authservice.service.facade.impl;

import com.example.authservice.mapper.UserMapper;
import com.example.authservice.model.dto.AddUserRequest;
import com.example.authservice.model.dto.AuthUserDto;
import com.example.authservice.model.dto.ChangePasswordRequest;
import com.example.authservice.model.dto.ChangeRolesRequest;
import com.example.authservice.model.exception.AuthenticationException;
import com.example.authservice.service.UserService;
import com.example.authservice.service.facade.UserFacade;
import com.example.authservice.service.validation.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public AuthUserDto getUser(Long id) {
        log.info("Getting GET getUser request for userId: {}", id);
        return mapper.toAuthUserDto(userService.findById(id));
    }

    @Override
    public List<AuthUserDto> getAllUsers() {
        log.info("Getting GET getAllUsers request");
        return userService.getAll().stream()
                .map(mapper::toAuthUserDto)
                .toList();
    }

    @Override
    public AuthUserDto removeUser(Long id) {
        log.info("Getting DELETE removeUser request for userId: {}", id);
        var userToDelete = userService.findById(id);
        userService.remove(userToDelete);
        return mapper.toAuthUserDto(userToDelete);
    }

    @Override
    public String changePasswordByUser(String principalUsername, ChangePasswordRequest changePasswordRequest) {
        log.info("Getting PATCH changePasswordByUser request from {} for {}", principalUsername, changePasswordRequest.getUsername());
        if (principalUsername == null || !principalUsername.equals(changePasswordRequest.getUsername())) {
            throw new AuthenticationException("You do not have authorities for that");
        }
        userService.changePassword(
                principalUsername,
                changePasswordRequest.getOldPassword(),
                changePasswordRequest.getNewPassword()
        );
        return "Password changed";
    }

    @Override
    public String changeOtherUserPassword(ChangePasswordRequest changePasswordRequest) {
        log.info("Getting PATCH changeOtherUserPassword request for {}", changePasswordRequest.getUsername());
        userService.changePassword(
                changePasswordRequest.getUsername(),
                changePasswordRequest.getOldPassword(),
                changePasswordRequest.getNewPassword()
        );
        return "Password changed";
    }
}
