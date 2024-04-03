package com.example.authservice.controller;

import com.example.authservice.model.dto.AddUserRequest;
import com.example.authservice.model.dto.AuthUserDto;
import com.example.authservice.model.dto.ChangeRolesRequest;
import com.example.authservice.model.dto.TokenDto;
import com.example.authservice.service.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('SYSTEM_ADMIN')")
    public ResponseEntity<AuthUserDto> createUser(@RequestBody AddUserRequest addUserRequest) {
        return ResponseEntity.ok(userFacade.addUser(addUserRequest));
    }

    @PutMapping("/{id}/change-roles")
    @PreAuthorize("hasAnyAuthority('SYSTEM_ADMIN', 'ROLE_MANIPULATOR')")
    public ResponseEntity<AuthUserDto> changeRoles(
            @PathVariable("id") Long id,
            @RequestBody ChangeRolesRequest changeRolesRequest
    ) {
        return ResponseEntity.ok(userFacade.changeRoles(id, changeRolesRequest));
    }
}
