package com.example.authservice.service.impl;

import com.example.authservice.model.dto.OuterUserDto;
import com.example.authservice.model.exception.AuthenticationException;
import com.example.authservice.model.exception.NotFoundException;
import com.example.authservice.service.AuthenticationService;
import com.example.authservice.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserDetailsService userService;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public String authenticateAngGenerateToken(OuterUserDto user) {
        try {
            log.debug("Authentication of user {}", user.getUsername());
            var authentication = authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
            var jwt = jwtService.generateToken(authentication);
            return BEARER_PREFIX + jwt;
        } catch (NotFoundException | BadCredentialsException e) {
            log.error("Error during authentication for {}", user.getUsername(), e);
            throw new AuthenticationException("Authentication failed!");
        }
    }

    private Authentication authenticate(Authentication authentication) {
        var username = authentication.getName();
        var password = String.valueOf(authentication.getCredentials());

        var userDetails = userService.loadUserByUsername(username);
        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
        } else {
            throw new BadCredentialsException("Bad credentials");
        }
    }
}
