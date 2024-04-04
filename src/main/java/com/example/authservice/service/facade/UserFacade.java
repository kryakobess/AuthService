package com.example.authservice.service.facade;

import com.example.authservice.model.dto.AddUserRequest;
import com.example.authservice.model.dto.AuthUserDto;
import com.example.authservice.model.dto.ChangePasswordRequest;
import com.example.authservice.model.dto.ChangeRolesRequest;

import java.util.List;

public interface UserFacade {
    AuthUserDto addUser(AddUserRequest addUserRequest);
    AuthUserDto changeRoles(Long id, ChangeRolesRequest changeRolesRequest);
    AuthUserDto getUser(Long id);
    List<AuthUserDto> getAllUsers();
    AuthUserDto removeUser(Long id);
    String changePasswordByUser(String principalUsername, ChangePasswordRequest changePasswordRequest);
    String changeOtherUserPassword(ChangePasswordRequest changePasswordRequest);
}
