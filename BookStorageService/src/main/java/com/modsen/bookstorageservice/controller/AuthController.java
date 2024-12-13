package com.modsen.bookstorageservice.controller;

import com.modsen.bookstorageservice.dto.request.CreateJwtRequest;
import com.modsen.bookstorageservice.dto.request.CreateUserRequest;
import com.modsen.bookstorageservice.dto.response.JwtResponse;
import com.modsen.bookstorageservice.dto.response.UserResponse;
import com.modsen.bookstorageservice.mapper.UserMapper;
import com.modsen.bookstorageservice.service.AuthService;
import com.modsen.bookstorageservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public JwtResponse login(@Validated @RequestBody CreateJwtRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    public UserResponse register(@Validated @RequestBody CreateUserRequest createUserRequest) {
        return userService.createUser(createUserRequest);

    }

    @PostMapping("/refresh")
    public JwtResponse refresh(@RequestBody String refreshToken) {
        return authService.refresh(refreshToken);
    }
}
