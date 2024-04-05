package com.example.authservice.controller;

import com.example.authservice.model.dto.*;
import com.example.authservice.service.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority(T(com.example.authservice.model.enums.AuthorityType).USER_MANIPULATOR)")
    public ResponseEntity<AuthUserDto> createUser(@RequestBody AddUserRequest addUserRequest) {
        return ResponseEntity.ok(userFacade.addUser(addUserRequest));
    }

    @PutMapping("/{id}/change-roles")
    @PreAuthorize("hasAuthority(T(com.example.authservice.model.enums.AuthorityType).USER_MANIPULATOR)")
    public ResponseEntity<AuthUserDto> changeRoles(
            @PathVariable("id") Long id,
            @RequestBody ChangeRolesRequest changeRolesRequest
    ) {
        return ResponseEntity.ok(userFacade.changeRoles(id, changeRolesRequest));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AuthUserDto> getUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userFacade.getUser(id));
    }

    @GetMapping("/get-all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AuthUserDto>> getAllUsers() {
        return ResponseEntity.ok(userFacade.getAllUsers());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole(T(com.example.authservice.model.enums.RoleType).ROLE_SYSTEM_ADMIN)")
    public ResponseEntity<AuthUserDto> removeUser(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userFacade.removeUser(id));
    }

    @PatchMapping("/self-change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> selfChangePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(userFacade.changePasswordByUser(username, changePasswordRequest));
    }

    @PatchMapping("/change-password-to-user")
    @PreAuthorize("hasRole(T(com.example.authservice.model.enums.RoleType).ROLE_SYSTEM_ADMIN)")
    public ResponseEntity<String> changeOtherUserPassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        return ResponseEntity.ok(userFacade.changeOtherUserPassword(changePasswordRequest));
    }
}
