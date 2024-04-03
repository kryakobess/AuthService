package com.example.authservice.service.facade;

import com.example.authservice.model.dto.AddUserRequest;
import com.example.authservice.model.dto.AuthUserDto;
import com.example.authservice.model.dto.ChangeRolesRequest;

public interface UserFacade {
    AuthUserDto addUser(AddUserRequest addUserRequest);
    AuthUserDto changeRoles(Long id, ChangeRolesRequest changeRolesRequest);
}
